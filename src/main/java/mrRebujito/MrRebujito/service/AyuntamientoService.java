package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.Roles;
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
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	public Optional<Ayuntamiento> findByUsername(String username) {
		return ayuntamientoRepository.findByUsername(username);
	}
	
	public Optional<Ayuntamiento> findAyuntamientoById(int id){
		return this.ayuntamientoRepository.findById(id);
	}
	
	public List<Ayuntamiento> findAllAyuntamiento() {
		return this.ayuntamientoRepository.findAll();
	}
	
	public Ayuntamiento saveAyuntamiento(Ayuntamiento ayuntamiento) {
		ayuntamiento.setRol(Roles.AYUNTAMIENTO);
		ayuntamiento.setPassword(passwordEncoder.encode(ayuntamiento.getPassword()));
		return ayuntamientoRepository.save(ayuntamiento);
	}
	
	public int getLicenciasAprobadas(int idAyuntamiento) {
        return licenciaRepository.countAprobadasByAyuntamientoId(idAyuntamiento, EstadoLicencia.APROBADA);
    }
	
	// Método para que el ayuntamiento actualice su propio perfil
	@Transactional
	public Ayuntamiento updateAyuntamiento(Ayuntamiento ayuntamientoU) {
		Ayuntamiento ayuntamiento = JWTUtils.userLogin();

	    if (ayuntamiento != null) {
	    	int licenciasAprobadas = getLicenciasAprobadas(ayuntamiento.getId());

	        if (ayuntamientoU.getLicenciaMax() < licenciasAprobadas) {
	            throw new IllegalArgumentException(
	                "No se puede reducir el número máximo de licencias por debajo de las aprobadas (" + licenciasAprobadas + ")"
	            );
	        }

	        ayuntamiento.setNombre(ayuntamientoU.getNombre());
	        ayuntamiento.setFoto(ayuntamientoU.getFoto());
	        ayuntamiento.setCorreo(ayuntamientoU.getCorreo());
	        ayuntamiento.setTelefono(ayuntamientoU.getTelefono());
	        ayuntamiento.setDireccion(ayuntamientoU.getDireccion());
	        ayuntamiento.setLicenciaMax(ayuntamientoU.getLicenciaMax());

	        return ayuntamientoRepository.save(ayuntamiento);
	    }

	    return null;
	}
	
	// Método para que ADMIN actualice cualquier ayuntamiento por ID
	@Transactional
	public Ayuntamiento updateAyuntamientoById(int id, Ayuntamiento ayuntamientoU) {
		Optional<Ayuntamiento> ayuntamientoOpt = ayuntamientoRepository.findById(id);

	    if (ayuntamientoOpt.isPresent()) {
	    	Ayuntamiento ayuntamiento = ayuntamientoOpt.get();
	    	
	    	int licenciasAprobadas = getLicenciasAprobadas(id);

	        if (ayuntamientoU.getLicenciaMax() < licenciasAprobadas) {
	            throw new IllegalArgumentException(
	                "No se puede reducir el número máximo de licencias por debajo de las aprobadas (" + licenciasAprobadas + ")"
	            );
	        }

	        ayuntamiento.setNombre(ayuntamientoU.getNombre());
	        ayuntamiento.setFoto(ayuntamientoU.getFoto());
	        ayuntamiento.setCorreo(ayuntamientoU.getCorreo());
	        ayuntamiento.setTelefono(ayuntamientoU.getTelefono());
	        ayuntamiento.setDireccion(ayuntamientoU.getDireccion());
	        ayuntamiento.setLicenciaMax(ayuntamientoU.getLicenciaMax());

	        return ayuntamientoRepository.save(ayuntamiento);
	    }

	    return null;
	}
	
	// Método para que el ayuntamiento borre su propio perfil
	@Transactional
	public boolean deleteAyuntamiento() {
		Ayuntamiento ayuntamiento = JWTUtils.userLogin();
		if (ayuntamiento != null) {
			ayuntamientoRepository.deleteById(ayuntamiento.getId());
			return true;
		}
		return false;
	}
	
	// Método para que ADMIN borre cualquier ayuntamiento por ID
	@Transactional
	public boolean deleteAyuntamientoById(int id) {
		Optional<Ayuntamiento> ayuntamientoOpt = ayuntamientoRepository.findById(id);
		if (ayuntamientoOpt.isPresent()) {
			ayuntamientoRepository.deleteById(id);
			return true;
		}
		return false;
	}
}