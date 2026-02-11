package mrRebujito.MrRebujito.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.service.SocioService;

//Permite realizar las operaciones GET, POST, PUT, DELETE a través de HTTP
@RestController

//Todo en esta clase empieza por socio
@RequestMapping("/socio")

//Para la documentación
@Tag(name = "Socios", description = "Controlador para la gestión de los socios")
public class SocioController {

	// Crea automáticamente el new SocioService
	@Autowired
	private SocioService socioService;

	@PostMapping
	public ResponseEntity<String> saveSocio(@RequestBody Socio socio) {
	    // 1. Comprobar si el username ya existe
	    if (socioService.findSocioByUsername(socio.getUsername()).isPresent()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	        		.body("Error: El nombre de usuario '" + socio.getUsername() + "' ya está en uso.");
	    }
	    
	    // 2. Intentar guardar
	    Socio guardado = socioService.saveSocio(socio);
	    
	    if (guardado != null) {
	        return ResponseEntity.status(HttpStatus.CREATED)
	        		.body("Socio registrado con éxito: " + guardado.getUsername());
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	        		.body("Error: No se ha podido procesar el registro del socio.");
	    }
	}

	@PutMapping
	@Operation(summary = "Actualizar un socio existente")
	public ResponseEntity<String> updateSocio(@RequestBody Socio updatedSocio) {
	    Socio response = socioService.updateSocio(updatedSocio);
	    
	    if (response != null) {
	        return ResponseEntity.ok("Datos del socio actualizados correctamente.");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Error: No se encontró el socio o no tiene permisos para actualizar.");
	    }
	}

	@DeleteMapping
	@Operation(summary = "Eliminar un socio logueado")
	public ResponseEntity<String> deleteSocio() {
	    if (socioService.deleteSocio()) {
	        return ResponseEntity.ok("Socio eliminado exitosamente del sistema.");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Error: No se pudo eliminar el socio. Es posible que no esté autenticado.");
	    }
	}

	@GetMapping("/misCasetas")
	public ResponseEntity<List<Caseta>> getMisCasetas() {
		List<Caseta> casetas = socioService.getMisCasetas();
		if (casetas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(casetas);
	}
	
	@GetMapping("/detalles")
	public ResponseEntity<Socio> verPerfil() {
		Socio socio = socioService.getSelf();
		if (socio == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(socio);
	}

	@GetMapping
	public ResponseEntity<List<Socio>> findAll() {
		return ResponseEntity.ok(socioService.findAllSocios());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener detalles de un socio por ID")
	public ResponseEntity<Socio> findById(@PathVariable int id) {
	    Optional<Socio> opSocio= socioService.findSocioById(id);
	    
	    if (opSocio.isPresent()) {
			return ResponseEntity.ok(opSocio.get());
		}
	    else {
	    	
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}
	
}