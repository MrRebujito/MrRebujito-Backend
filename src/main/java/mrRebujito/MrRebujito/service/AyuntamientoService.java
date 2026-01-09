package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.repository.AyuntamientoRepository;
import mrRebujito.MrRebujito.repository.SolicitudLicenciaRepository;
import  mrRebujito.MrRebujito.security.JWTUtils;

@Service
public class AyuntamientoService {
	@Autowired
    private SolicitudLicenciaRepository licenciaRepository;
	
	@Autowired 
	private AyuntamientoRepository ayuntamientoRepository;
	
	@Autowired
	private JWTUtils JWTUtils;

	public Optional<Ayuntamiento> findByUsername(String username) {
		return ayuntamientoRepository.findByUsername(username);
	}
	
	public Optional<Ayuntamiento> findById(int id){
		return this.ayuntamientoRepository.findById(id);
	}
	
	public List<Ayuntamiento> findAll() {
		return this.ayuntamientoRepository.findAll();
	}
	
	public Ayuntamiento save(Ayuntamiento ayuntamiento) {
		return this.ayuntamientoRepository.save(ayuntamiento);
	}
	public int getLicenciasAprobadas(int idAyuntamiento) {
        return licenciaRepository.countAprobadasByAyuntamientoId(idAyuntamiento, EstadoLicencia.APROBADA);
    }
	
	public Ayuntamiento update(int idAyuntamiento, Ayuntamiento ayuntamiento) {
	    Optional<Ayuntamiento> opAyuntamiento = ayuntamientoRepository.findById(idAyuntamiento);

	    if (opAyuntamiento.isPresent()) {
	        Ayuntamiento soc = opAyuntamiento.get();

	        int licenciasAprobadas = getLicenciasAprobadas(idAyuntamiento);

	        if (ayuntamiento.getLicenciaMax() < licenciasAprobadas) {
	            throw new IllegalArgumentException(
	                "No se puede reducir el número máximo de licencias por debajo de las aprobadas (" + licenciasAprobadas + ")"
	            );
	        }

	        soc.setNombre(ayuntamiento.getNombre());
	        soc.setFoto(ayuntamiento.getFoto());
	        soc.setCorreo(ayuntamiento.getCorreo());
	        soc.setTelefono(ayuntamiento.getTelefono());
	        soc.setDireccion(ayuntamiento.getDireccion());
	        soc.setLicenciaMax(ayuntamiento.getLicenciaMax());

	        return ayuntamientoRepository.save(soc);
	    }

	    return null;
	}
	public void delete(int id) {
		this.ayuntamientoRepository.deleteById(id);
	}
	
	
}
