package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	// Método para actualizar una solicitud que ya existe 
	public SolicitudLicencia update(int id, SolicitudLicencia solicitudDatos) {
		// Buscamos la solicicitud con el método por id
		Optional<SolicitudLicencia> oSolicitud = findById(id);
		
		// Compruebo si existe
		if (oSolicitud.isPresent()) {
			// En caso de que exista, obtenemos la instancia de solicitud
			SolicitudLicencia solicitud = oSolicitud.get();
			
			// Actualizamos los campos
			solicitud.setEstadoLicencia(solicitudDatos.getEstadoLicencia());
			
			
			return save(solicitud);
		}
		return null;
	}
	
	
	// Método para borrar una solicitud por su id
	public void delete(int id) {
		this.solicitudRepository.deleteById(id);
	}
}
