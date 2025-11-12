package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.repository.SocioRepository;

@Service
public class SocioService {

	@Autowired 
	private SocioRepository socioRepository;

	
	//Método para obtener un socio
	//Se le tiene que pasar un id, que es el generado por DomainEntity
	public Optional<Socio> findById(int id){
		
		//Llama al método de socioRepository, viene dado por el Jpa
		return this.socioRepository.findById(id);
	}
	
	
	//Método para obtener todos los socios
	public List<Socio> findAll() {
		
		return this.socioRepository.findAll();
	}
	
	//Método para guardar un socio, falta modificarlo para el estado de licencia
	public Socio save(Socio socio) {
		return this.socioRepository.save(socio);
	}
	
	
	//Método para actualizar un socio por id 
	public Socio update(int idSocio, Socio socio) {
		
		//Variable socio optional para encontrar el socio por id
		Optional<Socio> opSocio= findById(idSocio);
		
		//Comprueba si el socio existe
		if(opSocio.isPresent()) {
			
			//Si existe te guarda el socio y se hacen los cambios
			Socio soc = opSocio.get();
			
			soc.setNombre(socio.getNombre());
			soc.setPrimerApellido(socio.getPrimerApellido());
			soc.setSegundoApellido(socio.getSegundoApellido());
			soc.setFoto(socio.getFoto());
			soc.setCorreo(socio.getCorreo());
			soc.setDireccion(socio.getDireccion());
			soc.setTelefono(socio.getTelefono());
			
			//Te devuelve el socio guardado
			return save(soc);
		}
		
		//Si no existe te devuelve null
		return null;
		
	}
	
	//Método para eliminar por id 
	public void delete(int id) {
		this.socioRepository.deleteById(id);
	}
	
	
	//Método para listar y mostrar las casetas a las que pertence el socio
	public List<Caseta> getCasetasBySocioId(int socioId) {
		return this.socioRepository.findCasetasBySocioId(socioId);
	}
	
	//Método para listar todos los ayuntamientos
	public List<Ayuntamiento> findAllAyuntamientos() {
		return this.socioRepository.findAllAyuntamientos();
	}
	
	//Método para listar todas las casetas junto a sus cartas
	public List<Caseta> findAllCasetasConProductos() {
		return this.socioRepository.findAllCasetasConProductos();
	}
	
	
	//Método para listar los productos de una caseta en específico 
	public List<Producto> findProductosByCasetaId(int casetaId) {
		return this.socioRepository.findProductosByCasetaId(casetaId);
	}
	
}
