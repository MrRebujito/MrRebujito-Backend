package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.repository.AyuntamientoRepository;
import mrRebujito.MrRebujito.repository.SolicitudLicenciaRepository;
import mrRebujito.MrRebujito.security.JWTUtils;

@Service
public class SolicitudLicenciaService {
	@Autowired
	private SolicitudLicenciaRepository solicitudRepository;

	@Autowired
	private JWTUtils JWTUtils;

	@Autowired
	private AyuntamientoRepository ayuntamientoRepository;

	// Método para crear solicitud solo con ID de ayuntamiento
	@Transactional
	public SolicitudLicencia crearSolicitudConAyuntamiento(int ayuntamientoId) {
		// 1. Buscar el ayuntamiento por ID
		Optional<Ayuntamiento> ayuntamientoOpt = ayuntamientoRepository.findById(ayuntamientoId);
		if (!ayuntamientoOpt.isPresent()) {
			throw new IllegalArgumentException("Ayuntamiento con ID " + ayuntamientoId + " no encontrado");
		}

		Ayuntamiento ayuntamiento = ayuntamientoOpt.get();

		// 2. Crear nueva solicitud
		SolicitudLicencia nuevaSolicitud = new SolicitudLicencia();
		nuevaSolicitud.setAyuntamiento(ayuntamiento);
		nuevaSolicitud.setEstadoLicencia(EstadoLicencia.PENDIENTE);

		// 3. Asociar la solicitud con la caseta logueada (si aplica)
		// Esto depende de tu lógica de negocio

		// 4. Guardar la solicitud
		return solicitudRepository.save(nuevaSolicitud);
	}

	// Método existente para guardar solicitud completa
	public SolicitudLicencia saveSolicitud(SolicitudLicencia solicitud) {
		if (solicitud.getEstadoLicencia() == null) {
			solicitud.setEstadoLicencia(EstadoLicencia.PENDIENTE);
		}
		// Verificar que el ayuntamiento existe
		if (solicitud.getAyuntamiento() == null || solicitud.getAyuntamiento().getId() == 0) {
			throw new IllegalArgumentException("La solicitud debe tener un ayuntamiento válido");
		}
		return this.solicitudRepository.save(solicitud);
	}

	public SolicitudLicencia save(SolicitudLicencia s) {
		return this.solicitudRepository.save(s);
	}

	// Método para obtener una solicitud por id
	public Optional<SolicitudLicencia> findSolicitudById(int id) {
		return this.solicitudRepository.findById(id);
	}

	// Método para obtener todas las solicitudes
	public List<SolicitudLicencia> findSolicitudesAll() {
		return this.solicitudRepository.findAll();
	}

	public List<SolicitudLicencia> getAllSolicitudesByAyuntamiento() {
		Ayuntamiento ayuntamiento = JWTUtils.userLogin();
		return this.solicitudRepository.findAllByAyuntamiento(ayuntamiento);
	}

	@Transactional
	public SolicitudLicencia update(int idSolicitudLicencia, SolicitudLicencia solicitudLicenciaActualizada) {
		Optional<SolicitudLicencia> oSolicitudLicencia = findSolicitudById(idSolicitudLicencia);

		if (oSolicitudLicencia.isPresent()) {

			SolicitudLicencia solicitudExistente = oSolicitudLicencia.get();
			// Comprobamos que el auntamiento que actualiza la peticion es la de la licencia
			if (solicitudExistente.getAyuntamiento().equals(JWTUtils.userLogin())) {

				EstadoLicencia nuevoEstado = solicitudLicenciaActualizada.getEstadoLicencia();

				// Si el nuevo estado es 'APROBADA' aceptamos el update
				if (nuevoEstado.name().equals("APROBADA")) {

					Ayuntamiento ayuntamiento = solicitudExistente.getAyuntamiento();
					int ayuntamientoId = ayuntamiento.getId();

					// A. Contar las licencias activas
					long licenciasActivas = solicitudRepository.countApprovedLicensesByAyuntamiento(ayuntamientoId);
					int limiteMaximo = ayuntamiento.getLicenciaMax();

					// B. Validar la regla de negocio
					if (licenciasActivas >= limiteMaximo) {
						String mensaje = String.format(
								"Error de límite: El Ayuntamiento %s ya ha alcanzado su límite de licencias (%d).",
								ayuntamiento.getNombre(), limiteMaximo);
						throw new IllegalStateException(mensaje);
					}
				}

				// Si no hay problema, actualizamos y guardamos
				solicitudExistente.setEstadoLicencia(nuevoEstado);
				return saveSolicitud(solicitudExistente);
			}
			return null;
		}
		return null;
	}

	// Método para borrar una solicitud por su id
	@Transactional
	public boolean deleteSolicitud(int id) {
		try {
			Optional<SolicitudLicencia> solicitudO = solicitudRepository.findById(id);
			if (!solicitudO.isPresent()) {
				return false;
			}

			SolicitudLicencia solicitud = solicitudO.get();

			if (!solicitud.getEstadoLicencia().equals(EstadoLicencia.PENDIENTE)) {
				return false;
			}

			Object userLogin = JWTUtils.userLogin();
			if (!(userLogin instanceof Caseta)) {
				return false;
			}

			Caseta caseta = (Caseta) userLogin;

			if (!caseta.getSolicitudesLicencia().contains(solicitud)) {
				return false;
			}
			caseta.getSolicitudesLicencia().remove(solicitud);
			solicitudRepository.deleteById(id);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Método aceptar o rechazar solicitud
	public boolean aceptarSolicitud(int id) {
		boolean res = false;
		Optional<SolicitudLicencia> solicitudO = solicitudRepository.findById(id);
		if (solicitudO.isPresent()) {
			Ayuntamiento ayuntamiento = JWTUtils.userLogin();
			if (solicitudO.get().getAyuntamiento().equals(ayuntamiento)) {
				solicitudO.get().setEstadoLicencia(EstadoLicencia.APROBADA);
				solicitudRepository.save(solicitudO.get());
				res = true;
			}
		}
		return res;
	}

	public boolean rechazarSolicitud(int id) {
		boolean res = false;
		Optional<SolicitudLicencia> solicitudO = solicitudRepository.findById(id);
		if (solicitudO.isPresent()) {
			Ayuntamiento ayuntamiento = JWTUtils.userLogin();
			if (solicitudO.get().getAyuntamiento().equals(ayuntamiento)) {
				solicitudO.get().setEstadoLicencia(EstadoLicencia.RECHAZADA);
				solicitudRepository.save(solicitudO.get());
				res = true;
			}
		}
		return res;
	}

}
