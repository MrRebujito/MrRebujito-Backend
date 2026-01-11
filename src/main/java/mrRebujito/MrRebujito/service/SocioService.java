package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Roles;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.repository.SocioRepository;
import mrRebujito.MrRebujito.security.JWTUtils;

@Service
public class SocioService {

    @Autowired 
    private SocioRepository socioRepository;

    @Autowired
    private JWTUtils jwtUtils;
    
	@Autowired
    private PasswordEncoder passwordEncoder;
    
    // Método para obtener un socio
    public Optional<Socio> findSocioById(int id){
        return this.socioRepository.findById(id);
    }
    
    public Optional<Socio> findByUsername(String username) {
		return this.socioRepository.findByUsername(username);
	}
    
    // Método para obtener todos los socios
    public List<Socio> findAllSocios() {
        return this.socioRepository.findAll();
    }
    
    // Método para guardar un socio con contraseña encriptada
    public Socio saveSocio(Socio socio) {
        // Encriptar la contraseña antes de guardar
        if (socio.getPassword() != null && !socio.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(socio.getPassword());
            socio.setPassword(encryptedPassword);
        }
        
            socio.setRol(Roles.SOCIO);
        
        return this.socioRepository.save(socio);
    }
    
    // Método para actualizar un socio
    public Socio updateSocio(Socio socio) {
        Socio socioLogin = jwtUtils.userLogin();
        
        if(socioLogin != null) {
            socioLogin.setNombre(socio.getNombre());
            socioLogin.setPrimerApellido(socio.getPrimerApellido());
            socioLogin.setSegundoApellido(socio.getSegundoApellido());
            socioLogin.setFoto(socio.getFoto());
            socioLogin.setCorreo(socio.getCorreo());
            socioLogin.setDireccion(socio.getDireccion());
            socioLogin.setTelefono(socio.getTelefono());
            
            return this.socioRepository.save(socioLogin);
        }
        
        return null;
    }
    
    // Método para eliminar el socio logueado
    public boolean deleteSocio() {
        Socio socioLogin = jwtUtils.userLogin();
        
        if (socioLogin != null) {
            this.socioRepository.delete(socioLogin);
            return true;
        }
        return false;
    }
    
    
    // Método para listar las casetas del socio logueado
    public List<Caseta> getMisCasetas() {
        Socio socioLogin = jwtUtils.userLogin();
        
        if (socioLogin != null) {
            return this.socioRepository.findCasetasBySocioId(socioLogin.getId());
        }
        return null;
    }
    

    
    
    public Optional<Socio> findSocioByUsername(String username){
        return socioRepository.findByUsername(username);
    }
}