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
	
	public Ayuntamiento updateAyuntamiento(Ayuntamiento ayuntamientoU) {
		Ayuntamiento ayuntamiento = JWTUtils.userLogin();

	    if (ayuntamiento != null) {

	    	//Comprobamos el numero de licencias aprobadas para evitar actualizar un valor inferior de licencias 
	    	//totales del numero de licencias que tenemos aprobadas
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
	
	@Transactional
	public boolean deleteAyuntamiento() {
		Ayuntamiento ayuntamiento = JWTUtils.userLogin();
		if (ayuntamiento != null) {
			ayuntamientoRepository.deleteById(ayuntamiento.getId());
			return true;
		}
		return false;
	}
	
	
}
