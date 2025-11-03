package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Caseta;

import mrRebujito.MrRebujito.repository.CasetaRepository;


@Service
public class CasetaService {
	// Inyectamos la instancia de la clase CasetaRepository 
	@Autowired
	private CasetaRepository casetaRepository;
	
	// Método para obtener una caseta por su ID.
	public Optional<Caseta> findById(int id) {
		return this.casetaRepository.findById(id);
	}
	
	
	// Método para obtener todas las casetas
	public List<Caseta> findAll() {
		return this.casetaRepository.findAll();
	}
	
	
	// Método para guardar una caseta
	public Caseta save(Caseta caseta) {
		return casetaRepository.save(caseta);
	}
	
	
	// Método para actualizar una caseta
	public Caseta update(int id, Caseta casetaDatos) {
		// Busco la caseta por su id y la guardo en el Optional
		Optional<Caseta> oCaseta = findById(id);
		
		// Compruebo si la caseta existe 
		if (oCaseta.isPresent()) {
			// En caso de que exista obtengo el objeto
			Caseta caseta = oCaseta.get();
			
			//-- VALIDACIÓN AFORO DE LA CASETA --
			// Tenemos que contar cuantos socios tiene la caseta
			int numSocios = 0;
			
			// Comprobamos si la lista existe antes de accederla
			if (caseta.getListaSocios() != null) {
				numSocios = caseta.getListaSocios().size();
			}
			
			// Compruebo si el nuevo aforo que tenemos es menor que los socios actuales
			if (casetaDatos.getAforo() < numSocios) {
				// Si es menor, hacemos que no se actualice
				return null;
			}
			
			
			// Actualizo los campos que heredamos de Actor
			caseta.setNombre(casetaDatos.getNombre());
			caseta.setCorreo(casetaDatos.getCorreo());
			caseta.setTelefono(casetaDatos.getTelefono());
			caseta.setDireccion(casetaDatos.getDireccion());
			caseta.setFoto(casetaDatos.getFoto());
			
			// Actualizo los campos de Caseta
			caseta.setRazonS(casetaDatos.getRazonS());
			caseta.setAforo(casetaDatos.getAforo());
			caseta.setPublica(casetaDatos.isPublica());
			
			return save(caseta);
		}
		return null;
	}
	

	// Método para eliminar una caseta por id
	public void delete(int id) {
		this.casetaRepository.deleteById(id);
	}
}
