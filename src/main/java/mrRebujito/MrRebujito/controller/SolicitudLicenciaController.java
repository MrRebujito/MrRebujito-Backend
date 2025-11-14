package mrRebujito.MrRebujito.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
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
	private SolicitudLicenciaService solicitudLicenciaService;
	
	
	@GetMapping
    @Operation(summary = "Obtener todas las solicitudLicencias", description = "Devuelve una lista completa de todos las solicitudLicencias registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudLicencias obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Error: No se ha podido obtener la lista de solicitudLicencias.")
    })
    public ResponseEntity<List<SolicitudLicencia>> findAll() {
        return ResponseEntity.ok(solicitudLicenciaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitudLicencia por ID", description = "Busca una solicitudLicencia específico utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "SolicitudLicencia encontrado"),
            @ApiResponse(responseCode = "400", description = "SolicitudLicencia no encontrado")
    })
    public ResponseEntity<SolicitudLicencia> findById(@PathVariable int id) {
        Optional<SolicitudLicencia> oSolicitudLicencia = solicitudLicenciaService.findById(id);
        return oSolicitudLicencia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

   
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una solicitudLicencia", description = "Actualiza la información de una solicitudLicencia existente según su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "SolicitudLicencia actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error: SolicitudLicencia no encontrada o datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Error: Se ha producido un error interno del servidor al actualizar la solicitudLicencia") 
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody SolicitudLicencia solicitudLicencia) {
    	try {
    		if (solicitudLicenciaService.update(id, solicitudLicencia) == null) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SolicitudLicencia no encontrada");
    		} else {
    			return ResponseEntity.status(HttpStatus.OK).body("SolicitudLicencia actualizada correctamente");
    		}
    	} catch (IllegalStateException e) {
         // Capturamos la excepción de límite de licencias
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    	}
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una solicitudLicencia", description = "Elimina una solicitudLicencia existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "SolicitudLicencia eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error: SolicitudLicencia no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<SolicitudLicencia> oSolicitudLicencia = solicitudLicenciaService.findById(id);
        if (oSolicitudLicencia.isPresent()) {
        	solicitudLicenciaService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("SolicitudLicencia eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SolicitudLicencia no encontrado");
        }
    }
    
   
    

}
