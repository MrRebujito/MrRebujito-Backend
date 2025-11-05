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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

	@GetMapping
	@Operation(summary = "Obtener todos los administradores", description = "Devuelve todos los administradores registrados")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Lista de administradores obtenida exitosamente"),
			@ApiResponse(responseCode = "400", description = "No se pudo obtener la lista de administradores")
	})
	public ResponseEntity<List<Administrador>> findAll() {
		List<Administrador> admins = administradorService.findAll();
		if (admins.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(admins);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar administrador por id", description = "Busca un administrador específico utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador encontrado"),
			@ApiResponse(responseCode = "400", description = "Administrador no encontrado") 
	})
	public ResponseEntity<Administrador> findById(@PathVariable int id) {
		Optional<Administrador> opAdministrador = administradorService.findById(id);

		if (opAdministrador.isPresent()) {
			Administrador administrador = opAdministrador.get();
			return ResponseEntity.ok(administrador);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo administrador", description = "Registra un nuevo administrador en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador creado correctamente"),
			@ApiResponse(responseCode = "500", description = "Error interno al crear el administrador") 
	})
	public ResponseEntity<String> save(@RequestBody Administrador administrador) {
		administradorService.save(administrador);
		return ResponseEntity.status(HttpStatus.OK).body("Administrador creado correctamente");
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un administrador", description = "Actualiza la información de un administrador existente según su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Administrador no encontrado o datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno al actualizar el administrador") 
	})
	public ResponseEntity<String> update(@PathVariable int id, @RequestBody Administrador administrador) {

		if (administradorService.update(id, administrador) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Administrador no encontrado");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("Administrador actualizado correctamente");
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un administrador", description = "Elimina un administrador existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Administrador eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Administrador no encontrado") 
	})
	public ResponseEntity<String> delete(@PathVariable int id) {
		Optional<Administrador> opAdministrador = administradorService.findById(id);

		if (opAdministrador.isPresent()) {
			administradorService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("Administrador eliminado correctamente");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Administrador no encontrado");
		}
	}
}
