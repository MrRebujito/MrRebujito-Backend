package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Administrador;
import mrRebujito.MrRebujito.entity.Roles;
import mrRebujito.MrRebujito.repository.AdministradorRepository;
import mrRebujito.MrRebujito.security.JWTUtils;

@Service
public class AdministradorService {
	
	@Autowired 
	private AdministradorRepository administradorRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public JWTUtils JWTUtils;

    @Transactional
	public Administrador saveAdministrador(Administrador admin) {
		admin.setRol(Roles.ADMIN);
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		return administradorRepository.save(admin);
	}

	@Transactional
	public Administrador updateAdministrador(Administrador adminU) {
		Administrador admin = JWTUtils.userLogin();
		if (admin != null) {
			admin.setNombre(adminU.getNombre());
			admin.setFoto(adminU.getFoto());
			admin.setCorreo(adminU.getCorreo());
			admin.setTelefono(adminU.getTelefono());
			admin.setDireccion(adminU.getDireccion());
			admin.setPrimerApellido(adminU.getPrimerApellido());
			admin.setSegundoApellido(adminU.getSegundoApellido());
			return administradorRepository.save(admin);
		}
		return null;
	}

	public List<Administrador> getAllAdministradores() {
		return administradorRepository.findAll();
	}

	public Optional<Administrador> getAdministradorById(int id) {
		return administradorRepository.findById(id);
	}

	public Optional<Administrador> findByUsername(String username) {
		return administradorRepository.findByUsername(username);
	}

	@Transactional
	public boolean deleteAdministrador() {
		Administrador admin = JWTUtils.userLogin();
		if (admin != null) {
			administradorRepository.deleteById(admin.getId());
			return true;
		}
		return false;
	}
    
}
