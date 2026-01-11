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
import mrRebujito.MrRebujito.entity.Administrador;
import mrRebujito.MrRebujito.service.AdministradorService;

@RestController
@RequestMapping("/administrador")
@Tag(name = "Administradores", description = "Controlador para la gestión de los administradores")
public class AdministradorController {

	@Autowired
	private AdministradorService administradorService;


	@PostMapping
	@Operation(summary = "Crear un nuevo administrador", description = "Registra un nuevo administrador en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador creado correctamente"),
			@ApiResponse(responseCode = "400", description = "La foto debe ser un enlace válido"),
			@ApiResponse(responseCode = "500", description = "Error interno al crear el administrador") })
	public ResponseEntity<String> saveAdministrador(@RequestBody Administrador administrador) {
		String foto = administrador.getFoto();
		if (foto == null || !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
			return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
		}
		
		try {
			administradorService.saveAdministrador(administrador);
			return ResponseEntity.ok("Administrador creado correctamente");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error interno al crear el administrador");
		}
	}

	@PutMapping
	@Operation(summary = "Actualizar un administrador", description = "Actualiza la información de un administrador logueado")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Administrador no encontrado o datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno al actualizar el administrador") })
	public ResponseEntity<String> updateAdministrador(@PathVariable int id, @RequestBody Administrador administrador) {

		String foto = administrador.getFoto();
		if (foto == null || !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
			return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
		}

		if (administradorService.updateAdministrador(administrador) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Administrador no encontrado");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("Administrador actualizado correctamente");
		}
	}

	@DeleteMapping
	@Operation(summary = "Eliminar un administrador", description = "Elimina un administrador existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Administrador no encontrado") })
	public void deleteAdministrador() {
		if (administradorService.deleteAdministrador()) {
			ResponseEntity.status(HttpStatus.OK);
		} else {
			ResponseEntity.status(HttpStatus.NOT_FOUND);
		}
	}
}
