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
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.service.SolicitudLicenciaService;

@RestController
@RequestMapping("/solicitud")
@Tag(name = "Solicitud de licencia", description = "Controlador para la gestión de solicitudes de licencia")
public class SolicitudLicenciaController {
	@Autowired
	private SolicitudLicenciaService solicitudService;

	// Añadir este endpoint para crear solicitud solo con ID de ayuntamiento
	@PostMapping("/crear-con-ayuntamiento/{ayuntamientoId}")
	@Operation(summary = "Crear una solicitud con ID de ayuntamiento")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Solicitud creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "ID de ayuntamiento inválido"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<SolicitudLicencia> crearSolicitudConAyuntamiento(@PathVariable int ayuntamientoId) {
		SolicitudLicencia solicitud = solicitudService.crearSolicitudConAyuntamiento(ayuntamientoId);
		return ResponseEntity.ok(solicitud);
	}

	@PostMapping
	@Operation(summary = "Añadir una solicitud (objeto completo)")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Solicitud creada exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<SolicitudLicencia> save(@RequestBody SolicitudLicencia s) {
		SolicitudLicencia solicitud = solicitudService.saveSolicitud(s);
		return ResponseEntity.ok(solicitud);
	}

	@GetMapping
	@Operation(summary = "Obtener todas las solicitudes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<List<SolicitudLicencia>> findSolicitudesAll() {
		List<SolicitudLicencia> listSolicitud = solicitudService.findSolicitudesAll();
		return ResponseEntity.ok(listSolicitud);
	}

	@GetMapping("/Ayuntamiento")
	@Operation(summary = "Obtener todas las solicitudes de ayuntamiento")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de solicitudes de ayuntamiento obtenida exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	public ResponseEntity<List<SolicitudLicencia>> getAllSolicitudesByAyuntamiento() {
		List<SolicitudLicencia> listSolicitud = solicitudService.getAllSolicitudesByAyuntamiento();
		return ResponseEntity.ok(listSolicitud);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar una solicitud por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
			@ApiResponse(responseCode = "404", description = "Solicitud no encontrada, o emisor/receptor no logueado") })
	public ResponseEntity<SolicitudLicencia> findOneSolicitud(@PathVariable int id) {
		Optional<SolicitudLicencia> solicitud = solicitudService.findSolicitudById(id);
		if (solicitud.isPresent()) {
			return ResponseEntity.ok(solicitud.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PutMapping("/aceptar/{id}")
	@Operation(summary = "Aceptar una solicitud por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Solicitud aceptada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al aceptar la solicitud") })
	public ResponseEntity<String> acceptSolicitud(@PathVariable int id) { // Cambiado de void a ResponseEntity<String>
		Boolean verEstado = solicitudService.aceptarSolicitud(id);
		if (verEstado == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al aceptar la solicitud");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Solicitud aceptada correctamente");
		}
	}
	
	@PutMapping("/rechazar/{id}")
	@Operation(summary = "Rechazar una solicitud por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Solicitud rechazada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al rechazar la solicitud") })
	public ResponseEntity<String> rechazarSolicitud(@PathVariable int id) {
		Boolean verEstado = solicitudService.rechazarSolicitud(id);
		if (verEstado == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al rechazar la solicitud");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Solicitud rechazada correctamente");
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar una solicitud por ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Solicitud eliminada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al borrar la solicitud") })
	public ResponseEntity<String> delete(@PathVariable int id) { // Cambiado de void a ResponseEntity<String>
		Boolean verEstadoBorrado = solicitudService.deleteSolicitud(id);
		if (verEstadoBorrado == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al borrar la solicitud");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Solicitud eliminada correctamente");
		}
	}
}
