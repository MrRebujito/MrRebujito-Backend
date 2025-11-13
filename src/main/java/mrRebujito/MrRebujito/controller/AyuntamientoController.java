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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.service.AyuntamientoService;
import mrRebujito.MrRebujito.service.SolicitudLicenciaService;

//Permite realizar las operaciones GET, POST, PUT, DELETE a través de HTTP
@RestController

//Todo en esta clase empieza por socio
@RequestMapping("/ayuntamiento")

//Para la documentación
@Tag(name = "Ayuntamiento", description = "Controlador para la gestión del ayuntamiento")
public class AyuntamientoController {

	@Autowired
	private AyuntamientoService ayuntamientoService;
	
	SolicitudLicenciaService solicitudLicenciaService;
	
	@GetMapping
	@Operation(summary = "Obtener todos los ayuntamientos", description = "Te devuelve todos los ayuntamientos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Lista de ayuntamientos obtenida exitosamente") 
			})
	public ResponseEntity<List<Ayuntamiento>> findAll() {
		return ResponseEntity.ok(ayuntamientoService.findAll());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Buscar ayuntamiento por id", description = "Busca un ayuntamiento específico utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento encontrado"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado")
	})
	
	//Método que te devuelve un ayuntamiento
		public ResponseEntity<Ayuntamiento> findById(@PathVariable int id) {
		    Optional<Ayuntamiento> opAyuntamiento = ayuntamientoService.findById(id);
		    
		    //Si el Optional contiene un ayuntamiento
		    if (opAyuntamiento.isPresent()) {
		        Ayuntamiento ayuntamiento = opAyuntamiento.get();  
		        return ResponseEntity.ok(ayuntamiento); //Devolvemos 200 OK con el ayuntamiento
		    } 
		    //Si el Optional está vacío (no encontró el ayuntamiento)
		    else {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //Devolvemos 400 ERROR
		    }
		}
	
	@PostMapping //Peticiones HTTP POST
	@Operation(summary = "Crear un nuevo ayuntamiento", description = "Registra un nuevo ayuntamiento en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento creado correctamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el ayuntamiento") 
	})
	//Método para guardar Ayuntamiento
		public ResponseEntity<String> save(@RequestBody Ayuntamiento ayun) {
			if (ayuntamientoService.save(ayun)==null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ayuntamiento no creado correctamente");
			}
			return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento creado correctamente");
		}
	
	@PutMapping("/{id}") //Peticiones HTTP PUT
	@Operation(summary = "Actualizar un ayuntamiento", description = "Actualiza la información de un ayuntamiento existente según su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado o datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el ayuntamiento") 
	})
		public ResponseEntity<String> update(@PathVariable int id, @RequestBody Ayuntamiento ayun) {
			if (ayuntamientoService.update(id, ayun) == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ayuntamiento no encontrado");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Ayuntamiento actualizado correctamente");
			}
		}
	
	@DeleteMapping("/{id}") //Peticiones HTTP DELETE
	@Operation(summary = "Eliminar un ayuntamiento", description = "Elimina un ayuntamiento existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ayuntamiento eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Ayuntamiento no encontrado") 
	})
	public ResponseEntity<String> delete(@PathVariable int id) {
		Optional<Ayuntamiento> opAyuntamiento = ayuntamientoService.findById(id);
		
		if (opAyuntamiento.isPresent()) {
			ayuntamientoService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("Ayuntamieto eliminado correctamente");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ayuntamieto no encontrado");
		}
	}
	
	// Listar todas las solicitudes de este ayuntamiento
	@GetMapping("/{id}/solicitudes")
	public ResponseEntity<List<SolicitudLicencia>> listarSolicitudes(@PathVariable int id) {
	    return ResponseEntity.ok(solicitudLicenciaService.findByAyuntamiento(id));
	}

	// Ver una solicitud específica
	@GetMapping("/solicitud/{idSolicitud}")
	public ResponseEntity<SolicitudLicencia> mostrarSolicitud(@PathVariable int idSolicitud) {
		Optional<SolicitudLicencia> solicitud = solicitudLicenciaService.findById(idSolicitud);
	    return solicitud.map(ResponseEntity::ok)
	                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Cambiar estado de una solicitud (aceptar o rechazar)
	@PutMapping("/solicitud/{idSolicitud}/estado/{nuevoEstado}")
	public ResponseEntity<String> cambiarEstadoSolicitud(
	        @PathVariable int idSolicitud,
	        @PathVariable EstadoLicencia nuevoEstado) {

	    SolicitudLicencia solicitud = solicitudLicenciaService.actualizarEstado(idSolicitud, nuevoEstado);
	    if (solicitud == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Solicitud no encontrada o no actualizada");
	    }
	    return ResponseEntity.ok("Estado actualizado a " + nuevoEstado);
	}

	// 🔹 4. Listar casetas aprobadas por este ayuntamiento
	@GetMapping("/{id}/casetas-aprobadas")
	public ResponseEntity<List<Caseta>> listarCasetasAprobadas(@PathVariable int id) {
	    List<SolicitudLicencia> solicitudes = solicitudLicenciaService.findAprobadasByAyuntamiento(id);
	    List<Caseta> casetas = solicitudes.stream()
	            .map(SolicitudLicencia::getCaseta)
	            .toList();
	    return ResponseEntity.ok(casetas);
	}
	
	
	
	
}
