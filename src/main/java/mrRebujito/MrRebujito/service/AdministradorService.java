package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Administrador;
import mrRebujito.MrRebujito.repository.AdministradorRepository;

@Service
public class AdministradorService {
	
	@Autowired 
	private AdministradorRepository administradorRepository;

	public Optional<Administrador> findById(int id) {
		return administradorRepository.findById(id);
	}
	
	public List<Administrador> findAll() {
		return administradorRepository.findAll();
	}
	
	public Administrador save(Administrador administrador) {
		return administradorRepository.save(administrador);
	}
	
	public Administrador update(int idAdministrador, Administrador datosActualizados) {
		Optional<Administrador> opAdministrador = administradorRepository.findById(idAdministrador);
		
		if (opAdministrador.isPresent()) {
			Administrador admin = opAdministrador.get();
			admin.setNombre(datosActualizados.getNombre());
			admin.setPrimerApellido(datosActualizados.getPrimerApellido());
			admin.setSegundoApellido(datosActualizados.getSegundoApellido());
			admin.setFoto(datosActualizados.getFoto());
			admin.setCorreo(datosActualizados.getCorreo());
			admin.setDireccion(datosActualizados.getDireccion());
			admin.setTelefono(datosActualizados.getTelefono());
			
			return administradorRepository.save(admin);
		} else {
			return null;
		}
	}
	
	public void delete(int id) {
		administradorRepository.deleteById(id);
	}
}
