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
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.service.AyuntamientoService;

@RestController
@RequestMapping("/ayuntamiento")
@Tag(name = "Ayuntamiento", description = "Controlador para la gestión del ayuntamiento")
public class AyuntamientoController {

	@Autowired
	private AyuntamientoService ayuntamientoService;

	@GetMapping
	@Operation(summary = "Obtener todos los ayuntamientos", description = "Te devuelve todos los ayuntamientos")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de ayuntamientos obtenida exitosamente"),
			@ApiResponse(responseCode = "404", description = "No se pudo obtener la lista de ayuntamientos") })
	public ResponseEntity<List<Ayuntamiento>> findAllAyuntamiento() {
		List<Ayuntamiento> ayuntamientos = ayuntamientoService.findAllAyuntamiento();
		if (ayuntamientos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(ayuntamientos);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar ayuntamiento por id", description = "Busca un ayuntamiento específico utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento encontrado"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado", content = @Content) })
	public ResponseEntity<Ayuntamiento> findAyuntamientoById(@PathVariable int id) {
		Optional<Ayuntamiento> opAyuntamiento = ayuntamientoService.findAyuntamientoById(id);

		if (opAyuntamiento.isPresent()) {
			return ResponseEntity.ok(opAyuntamiento.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo ayuntamiento", description = "Registra un nuevo ayuntamiento en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento creado correctamente"),
			@ApiResponse(responseCode = "400", description = "La foto debe ser un enlace válido"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el ayuntamiento") })
	public ResponseEntity<String> saveAyuntamiento(@RequestBody Ayuntamiento ayun) {
		String foto = ayun.getFoto();
		if (foto != null && !foto.isEmpty() && !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
			return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
		}
		
		try {
			if (ayuntamientoService.findByUsername(ayun.getUsername()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El username ya está en uso");
			} else {
				Ayuntamiento a = ayuntamientoService.saveAyuntamiento(ayun);
				if (a != null) {
					return ResponseEntity.ok("Ayuntamiento creado correctamente");
				}
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error interno al crear el ayuntamiento");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Error interno al crear el ayuntamiento");
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un ayuntamiento por ID (Solo ADMIN)", description = "Actualiza la información de un ayuntamiento existente según su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado o datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el ayuntamiento") })
	public ResponseEntity<String> updateById(@PathVariable int id, @RequestBody Ayuntamiento ayun) {
		try {
			String foto = ayun.getFoto();
			if (foto != null && !foto.isEmpty() && !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
				return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
			}
			
			if (ayuntamientoService.updateAyuntamientoById(id, ayun) == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ayuntamiento no encontrado");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento actualizado correctamente");
			}
		} catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede actualizar el numero de licencias por debajo de las licencias vigentes");
	    }
	}
	
	@PutMapping
	@Operation(summary = "Actualizar mi perfil de ayuntamiento", description = "El ayuntamiento logueado actualiza su propia información")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<String> updateSelf(@RequestBody Ayuntamiento ayun) {
		try {
			String foto = ayun.getFoto();
			if (foto != null && !foto.isEmpty() && !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
				return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
			}
			
			if (ayuntamientoService.updateAyuntamiento(ayun) == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento actualizado correctamente");
			}
		} catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede actualizar el numero de licencias por debajo de las licencias vigentes");
	    }
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un ayuntamiento por ID (Solo ADMIN)", description = "Elimina un ayuntamiento existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado") })
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		Optional<Ayuntamiento> opAyuntamiento = ayuntamientoService.findAyuntamientoById(id);

		if (opAyuntamiento.isPresent()) {
			ayuntamientoService.deleteAyuntamientoById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento eliminado correctamente");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ayuntamiento no encontrado");
		}
	}
	
	@DeleteMapping
	@Operation(summary = "Eliminar mi perfil de ayuntamiento", description = "El ayuntamiento logueado elimina su propio perfil")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Error al eliminar") })
	public ResponseEntity<String> deleteSelf() {
		if (ayuntamientoService.deleteAyuntamiento()) {
			return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento eliminado correctamente");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar ayuntamiento");
		}
	}
}