package mrRebujito.MrRebujito.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import mrRebujito.MrRebujito.entity.Actor;
import mrRebujito.MrRebujito.entity.ActorLogin;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Roles;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.service.ActorService;
import mrRebujito.MrRebujito.service.AyuntamientoService;
import mrRebujito.MrRebujito.service.CasetaService;
import mrRebujito.MrRebujito.service.SocioService;
import mrRebujito.MrRebujito.security.JWTUtils;

@RestController
public class ActorController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtils JWTUtils;

	@Autowired
	private ActorService actorService;

	@Autowired
	private AyuntamientoService ayuntamientoService;
	
	@Autowired
	private CasetaService casetaService;
	
	@Autowired
	private SocioService socioService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody ActorLogin actorLogin) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(actorLogin.getUsername(), actorLogin.getPassword()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String token = JWTUtils.generateToken(authentication);
	    return ResponseEntity.ok(token); // Devuelve solo el token como string
	}

	@Operation(summary = "Banear a un actor (ayuntamiento, administrador o caseta) por ID")
	@PutMapping("/banear/{actorId}")
	public ResponseEntity<String> banearChef(@PathVariable int actorId) {
		Optional<Actor> actorO = actorService.findById(actorId);
		if (actorO.isPresent() && actorO.get().getRol() != Roles.ADMIN) {
			if (actorO.get().getRol() == Roles.CASETA) {
				Caseta c = casetaService.findCasetaById(actorO.get().getId()).get();
				c.setBaneado(true);
				actorService.saveBasico(c);
				return ResponseEntity.ok("Actor con ID " + actorId + " ha sido baneado.");
			} else if (actorO.get().getRol() == Roles.AYUNTAMIENTO) {
				Ayuntamiento a = ayuntamientoService.findAyuntamientoById(actorO.get().getId()).get();
				a.setBaneado(true);
				actorService.saveBasico(a);
				return ResponseEntity.ok("Ayuntamiento con ID " + actorId + " ha sido baneado.");
			} else {
				Socio s = socioService.findSocioById(actorO.get().getId()).get();
				s.setBaneado(true);
				actorService.saveBasico(s);
				return ResponseEntity.ok("Socio con ID " + actorId + " ha sido baneado.");
			}
			
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un chef con ID " + actorId);
		}
	}

	@Operation(summary = "Desbanear a un actor (ayuntamiento, administrador o caseta) por ID")
	@PutMapping("/desbanear/{actorId}")
	public ResponseEntity<String> desbanearChef(@PathVariable int actorId) {
		Optional<Actor> actorO = actorService.findById(actorId);
		if (actorO.isPresent() && actorO.get().getRol() != Roles.ADMIN) {
			actorO.get().setBaneado(false);
			actorService.saveBasico(actorO.get());
			return ResponseEntity.ok("Actor con ID " + actorId + " ha sido baneado.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un actor con ID " + actorId);
		}
	}
}