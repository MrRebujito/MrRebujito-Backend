package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.repository.SolicitudLicenciaRepository;

@Service
public class SolicitudLicenciaService {
	// Inyectamos la instancia de la clase SolicitudLicenciaRepository 
	@Autowired
	private SolicitudLicenciaRepository solicitudRepository;
	
	
	// Método para obtener una solicitud por id
	public Optional<SolicitudLicencia> findById(int id) {
		return this.solicitudRepository.findById(id);
	}
	
	
	// Método para obtener todas las solicitudes
	public List<SolicitudLicencia> findAll() {
		return this.solicitudRepository.findAll();
	}
	
	
	// Método para guardar una nueva solicitud
	public SolicitudLicencia save(SolicitudLicencia solicitud) {
		return this.solicitudRepository.save(solicitud);
	}
	
	
	@Transactional
	public SolicitudLicencia update(int idSolicitudLicencia, SolicitudLicencia solicitudLicenciaActualizada) {
	    Optional<SolicitudLicencia> oSolicitudLicencia = findById(idSolicitudLicencia);

	    if (oSolicitudLicencia.isPresent()) {
	        SolicitudLicencia solicitudExistente = oSolicitudLicencia.get();
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
	                String mensaje = String.format("Error de límite: El Ayuntamiento %s ya ha alcanzado su límite de licencias (%d).", 
	                                                ayuntamiento.getNombre(), limiteMaximo);
	                throw new IllegalStateException(mensaje);
	            }
	        }
	        
	        // Si no hay problema, actualizamos y guardamos
	        solicitudExistente.setEstadoLicencia(nuevoEstado);
	        return save(solicitudExistente);
	    }
	    return null;
	}
	
	
	// Método para borrar una solicitud por su id
	public void delete(int id) {
		this.solicitudRepository.deleteById(id);
	}
	

    public List<SolicitudLicencia> findByAyuntamiento(int ayuntamientoId) {
        return solicitudRepository.findByAyuntamientoId(ayuntamientoId);
    }

    public List<SolicitudLicencia> findAprobadasByAyuntamiento(int ayuntamientoId) {
        return solicitudRepository.findByAyuntamientoIdAndEstadoLicencia(
                ayuntamientoId, EstadoLicencia.APROBADA);
    }


    public SolicitudLicencia actualizarEstado(int id, EstadoLicencia nuevoEstado) {
        Optional<SolicitudLicencia> opSolicitud = solicitudRepository.findById(id);
        if (opSolicitud.isPresent()) {
            SolicitudLicencia solicitud = opSolicitud.get();
            solicitud.setEstadoLicencia(nuevoEstado);
            return solicitudRepository.save(solicitud);
        }
        return null;
    }
}
