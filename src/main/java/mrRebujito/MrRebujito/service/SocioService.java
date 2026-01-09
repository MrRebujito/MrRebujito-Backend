package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    // Método para obtener un socio
    public Optional<Socio> findById(int id){
        return this.socioRepository.findById(id);
    }
    
    // Método para obtener todos los socios
    public List<Socio> findAll() {
        return this.socioRepository.findAll();
    }
    
    // Método para guardar un socio con contraseña encriptada
    public Socio save(Socio socio) {
        // Encriptar la contraseña antes de guardar
        if (socio.getPassword() != null && !socio.getPassword().isEmpty()) {
            String encryptedPassword = bCryptPasswordEncoder.encode(socio.getPassword());
            socio.setPassword(encryptedPassword);
        }
        
        // Asignar rol si no está asignado
        if (socio.getRol() == null) {
            socio.setRol(Roles.SOCIO);
        }
        
        return this.socioRepository.save(socio);
    }
    
    // Método para actualizar un socio
    public Socio update(Socio socio) {
        Socio socioLogin = jwtUtils.userLogin();
        
        if(socioLogin != null) {
            socioLogin.setNombre(socio.getNombre());
            socioLogin.setPrimerApellido(socio.getPrimerApellido());
            socioLogin.setSegundoApellido(socio.getSegundoApellido());
            socioLogin.setFoto(socio.getFoto());
            socioLogin.setCorreo(socio.getCorreo());
            socioLogin.setDireccion(socio.getDireccion());
            socioLogin.setTelefono(socio.getTelefono());
            
            // Si se proporciona nueva contraseña, encriptarla
            if (socio.getPassword() != null && !socio.getPassword().isEmpty()) {
                String encryptedPassword = bCryptPasswordEncoder.encode(socio.getPassword());
                socioLogin.setPassword(encryptedPassword);
            }
            
            return save(socioLogin);
        }
        
        return null;
    }
    
    // Método para eliminar el socio logueado
    public void delete() {
        Socio socioLogin = jwtUtils.userLogin();
        
        if (socioLogin != null) {
            this.socioRepository.delete(socioLogin);
        }
    }
    
    
    // Método para listar las casetas del socio logueado
    public List<Caseta> getMisCasetas() {
        Socio socioLogin = jwtUtils.userLogin();
        
        if (socioLogin != null) {
            return this.socioRepository.findCasetasBySocioId(socioLogin.getId());
        }
        return null;
    }
    

    
    // Método para obtener el socio logueado
    public Socio getSocioLogueado() {
        return jwtUtils.userLogin();
    }
    
    public Optional<Socio> findByUsername(String username){
        return socioRepository.findByUsername(username);
    }
}