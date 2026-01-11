package mrRebujito.MrRebujito.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.security.JWTUtils;
import mrRebujito.MrRebujito.service.CasetaService;

@RestController
@RequestMapping("/caseta")
@Tag(name = "Casetas", description = "Controlador para la gestión de casetas")
public class CasetaController {

    @Autowired
    private CasetaService casetaService;
    
    @Autowired
    private JWTUtils jwtUtils;
    

    @GetMapping
	@Operation(summary = "Obtener todas las casetas")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista de casetas obtenida exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<List<Caseta>> getAllCasetas() {
		List<Caseta> casetas = casetaService.findAllCaseta();
		return ResponseEntity.ok(casetas);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar una caseta por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Caseta encontrada"),
			@ApiResponse(responseCode = "404", description = "Caseta no encontrada") })
	public ResponseEntity<Caseta> findOneCaseta(@PathVariable int id) {
		Optional<Caseta> caseta = casetaService.findCasetaById(id);
		if (caseta.isPresent()) {
			return ResponseEntity.ok(caseta.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/socios")
	@Operation(summary = "Obtener todos los socios de la caseta")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Socios obtenidos correctamente"),
			@ApiResponse(responseCode = "404", description = "No se encontraron socios") })
	public ResponseEntity<List<Socio>> getAllSociosByCaseta() {
		List<Socio> socios = casetaService.getAllSociosByCaseta();
		if (socios == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.ok(socios);
		}
	}

	@GetMapping("/eliminarSocio/{idSocio}")
	@Operation(summary = "Elimina un socio de la lista de socios de la caseta del usuario logueado.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Socio eliminado correctamente"),
			@ApiResponse(responseCode = "404", description = "Socio no encontrado en la caseta") })
	public ResponseEntity<String> eliminarSocio(@PathVariable int idSocio) {
		Caseta result = casetaService.removeSocio(idSocio);
		if (result != null) {
			return ResponseEntity.ok("Socio eliminado correctamente.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/anadirSocio/{idSocio}")
	@Operation(summary = "Añade un socio a la lista de socios de la caseta del usuario logueado.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "201", description = "Socio añadido correctamente"),
	    @ApiResponse(responseCode = "400", description = "Socio no encontrado o aforo excedido")
	})
	public ResponseEntity<String> anadirSocio(@PathVariable int idSocio) {
	    Caseta result = casetaService.addSocio(idSocio);
	    if (result != null) {
	        return ResponseEntity.status(HttpStatus.CREATED).body("Socio añadido correctamente");
	    }
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo añadir el socio");
	}

	
	@GetMapping("/carta/{id}")
	@Operation(summary = "Carta de una caseta por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Carta de una caseta encontrada"),
			@ApiResponse(responseCode = "404", description = "Caseta no encontrada") })
	public ResponseEntity<List<Producto>> findCartaByCaseta(@PathVariable int id) {
		Optional<Caseta> caseta = casetaService.findCasetaById(id);
		if (caseta.isPresent()) {
			return ResponseEntity.ok(caseta.get().getProductos());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/carta")
	@Operation(summary = "Carta de una caseta logueada")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Carta de una caseta encontrada"),
			@ApiResponse(responseCode = "404", description = "Caseta no encontrada") })
	public ResponseEntity<List<Producto>> findCartaByCasetaLogin() {
		List<Producto> carta = casetaService.getCartaCaseta();
		if (carta != null) {
			return ResponseEntity.ok(carta);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}


	@PostMapping
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Caseta creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
			@ApiResponse(responseCode = "409", description = "El username ya está en uso") })
	public void saveCaseta(@RequestBody Caseta caseta) {
		if (casetaService.findCasetaById(caseta.getId()).isPresent()) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			Caseta c = casetaService.saveCaseta(caseta);
			if (c != null) {
				ResponseEntity.status(HttpStatus.CREATED);
			}
		}
	}

	@PutMapping
	@Operation(summary = "Actualizar una caseta existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Caseta actualizada exitosamente"),
			@ApiResponse(responseCode = "404", description = "Caseta no encontrada"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida") })
	public void updateCaseta(@RequestBody Caseta updatedCaseta) {
		Caseta response = casetaService.updateCaseta(updatedCaseta);
		if (response != null) {
			ResponseEntity.status(HttpStatus.OK).build();
		} else {
			ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping
	@Operation(summary = "Eliminar una caseta logueada")
	@ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", description = "Caseta eliminada exitosamente"),
	    @ApiResponse(responseCode = "404", description = "Caseta no eliminada") 
	})
	public ResponseEntity<Void> deleteCaseta() {
	    if (casetaService.deleteCaseta()) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	 @PostMapping("/solicitud/{ayuntamientoId}")
	    public ResponseEntity<String> crearSolicitud(@PathVariable int ayuntamientoId) {
	        try {
	            casetaService.crearSolicitud(ayuntamientoId);
	            return ResponseEntity.ok("Solicitud creada correctamente.");
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }
	
}