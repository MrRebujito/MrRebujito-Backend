package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.repository.AyuntamientoRepository;

@Service
public class AyuntamientoService {

	@Autowired 
	private AyuntamientoRepository ayuntamientoRepository;
	
	public Optional<Ayuntamiento> findById(int id){
		return this.ayuntamientoRepository.findById(id);
	}
	
	public List<Ayuntamiento> findAll() {
		return this.ayuntamientoRepository.findAll();
	}
	
	public Ayuntamiento save(Ayuntamiento ayuntamiento) {
		return this.ayuntamientoRepository.save(ayuntamiento);
	}
	
	public Ayuntamiento update(int idAyuntamiento, Ayuntamiento ayuntamiento) {
		//Variable ayuntamiento optional para encontrar el ayuntamiento por id
		Optional<Ayuntamiento> opAyuntamiento= findById(idAyuntamiento);
		//Comprueba si el Ayuntamiento existe
		if(opAyuntamiento.isPresent()) {
			//Si existe te guarda el ayuntamiento y se hacen los cambios
			Ayuntamiento soc = opAyuntamiento.get();
			soc.setNombre(ayuntamiento.getNombre());
			soc.setFoto(ayuntamiento.getFoto());
			soc.setCorreo(ayuntamiento.getCorreo());
			soc.setTelefono(ayuntamiento.getTelefono());
			soc.setDireccion(ayuntamiento.getDireccion());
			soc.setLicenciaMax(ayuntamiento.getLicenciaMax());
			//Te devuelve el socio guardado
			return save(soc);
		}
		//Si no existe te devuelve null
		return null;
		
	}
	
	public void delete(int id) {
		this.ayuntamientoRepository.deleteById(id);
	}
	
	
}
