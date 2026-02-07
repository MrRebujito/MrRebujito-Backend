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
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Socio creado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
			@ApiResponse(responseCode = "409", description = "El username ya está en uso") })
	public void saveSocio(@RequestBody Socio socio) {
		if (socioService.findSocioByUsername(socio.getUsername()).isPresent()) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST);
		} else {
			Socio s = socioService.saveSocio(socio);
			if (s != null) {
				ResponseEntity.status(HttpStatus.CREATED);
			} else {
				ResponseEntity.status(HttpStatus.BAD_REQUEST);
			}
		}
	}

	@PutMapping
	@Operation(summary = "Actualizar un socio existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Socio actualizado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Socio no encontrado"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida") })
	public void updateSocio(@RequestBody Socio updatedSocio) {
		Socio response = socioService.updateSocio(updatedSocio);
		if (response != null) {
			ResponseEntity.status(HttpStatus.OK);
		} else {
			ResponseEntity.status(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	@Operation(summary = "Eliminar un socio logueado")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Socio eliminado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Socio no encontrado") })
	public void deleteSocio() {
		if (socioService.deleteSocio()) {
			ResponseEntity.status(HttpStatus.OK);
		} else {
			ResponseEntity.status(HttpStatus.NOT_FOUND);
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
	
	@GetMapping
	@Operation(summary = "Listar todos los socios")
	public ResponseEntity<List<Socio>> getAllSocios() {
	    List<Socio> socios = socioService.findAllSocios();
	    return ResponseEntity.ok(socios);
	}
}