package mrRebujito.MrRebujito.controller;

import java.io.IOException;
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
import jakarta.servlet.http.HttpServletResponse;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.service.CasetaService;

@RestController
@RequestMapping("/caseta")
@Tag(name = "Casetas", description = "Controlador para la gestión de casetas")
public class CasetaController {
	@Autowired
	private CasetaService casetaService;
	
	@GetMapping
    @Operation(summary = "Obtener todos los préstamos", description = "Devuelve una lista completa de todas las casetas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de casetas obtenida correctamente")
    })
	public ResponseEntity<List<Caseta>> findAll() {
		return ResponseEntity.ok(casetaService.findAll());
	}
	
	
	@GetMapping("/{id}")
	@Operation(summary = "Buscar caseta por Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Caseta encontrada"),
			@ApiResponse(responseCode = "400", description = "Caseta no encontrada")
	})
	public ResponseEntity<Caseta> findByid(@PathVariable int id) {
		Optional<Caseta> oCaseta = casetaService.findById(id);
		return oCaseta.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
	}
	
	
	@PostMapping
	@Operation(summary = "Crear una nueva caseta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Caseta creada correctamente"),
			@ApiResponse(responseCode = "500", description = "Error: Se ha producido un error al crear la caseta")
	})
	public void save(@RequestBody Caseta caseta, HttpServletResponse response) throws IOException {
		casetaService.save(caseta);
		response.setStatus(HttpStatus.OK.value());
		response.getWriter().println("Caseta creada correctamente");
	}
	
	
	@PutMapping("/{id}")
    @Operation(summary = "Actualizar una caseta", description = "Actualiza una caseta, validando el aforo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caseta actualizada"),
        @ApiResponse(responseCode = "400", description = "Caseta no encontrada"),
        @ApiResponse(responseCode = "410", description = "[ERROR]: El aforo no puede ser menor que el número de socios")
    })
    public void update(@PathVariable int id, @RequestBody Caseta caseta, HttpServletResponse response) throws IOException {
        
        Caseta casetaActualizada = casetaService.update(id, caseta);

        if (casetaActualizada == null) {         
            Optional<Caseta> oCaseta = casetaService.findById(id);
            if (oCaseta.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().println("Caseta no encontrada");
            } else {
                response.setStatus(HttpStatus.CONFLICT.value());
                response.getWriter().println("No se puede actualizar: el aforo es menor que el número de socios inscritos");
            }
        } else {
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().println("Caseta actualizada correctamente");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una caseta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caseta eliminada"),
        @ApiResponse(responseCode = "400", description = "Caseta no encontrada"),
        @ApiResponse(responseCode = "410", description = "Conflicto: No se puede eliminar, tiene socios inscritos")
    })
    public void delete(@PathVariable int id, HttpServletResponse response) throws IOException {
        Optional<Caseta> oCaseta = casetaService.findById(id);

        if (oCaseta.isPresent()) {
            Caseta caseta = oCaseta.get();
            int numSocios = 0;
            if (caseta.getListaSocios() != null) {
                numSocios = caseta.getListaSocios().size();
            }

            if (numSocios > 0) {
                response.setStatus(HttpStatus.CONFLICT.value());
                response.getWriter().println("No se puede eliminar la caseta, tiene socios inscritos");
            } else {
                casetaService.delete(id);
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().println("Caseta eliminada correctamente");
            }
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().println("Caseta no encontrada");
        }
    }
    
    //Métodos para las relaciones
    
    @PostMapping("/{casetaId}/solicitud/{ayuntamientoId}")
    @Operation(summary = "Crear una solicitud de licencia", description = "La caseta realiza una solicitud de licencia a un ayuntamiento.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear la solicitud")
    })
    public ResponseEntity<String> crearSolicitud(
    		@PathVariable int casetaId,
    		@PathVariable int ayuntamientoId) {
    	try {
    		casetaService.crearSolicitud(casetaId, ayuntamientoId);
    		return ResponseEntity.ok("Solicitud creada correctamente y añadida a la caseta.");
    		} catch (IllegalStateException e) {
    			// Capturamos el error de la regla de negocio (ya existe una solicitud/licencia)
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    			} catch (RuntimeException e) {
    				// Capturamos errores de datos (Caseta/Ayuntamiento no encontrado)
    				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    				}
    }
    
    @PostMapping("/{socioId}/añadirSocio/{casetaId}")
    @Operation(summary = "Añade un socio a la caseta", description = "La caseta introduce un socio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Socio añadido correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al añadir al socio")
    })
    public ResponseEntity<String> addSocio(
            @PathVariable int socioId,
            @PathVariable int casetaId) {

        try {
        	casetaService.addSocio(casetaId, socioId);
            return ResponseEntity.ok("Socio añadido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo añadir al socio correctamente");
        }
    }
    
    @PostMapping("/{productoId}/añadirProducto/{casetaId}")
    @Operation(summary = "Añade un producto a la caseta", description = "La caseta añade un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto añadido correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al añadir al socio")
    })
    public ResponseEntity<String> addProducto(
            @PathVariable int productoId,
            @PathVariable int casetaId) {

        try {
        	casetaService.addProducto(casetaId, productoId);
            return ResponseEntity.ok("Producto añadido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo añadir el producto correctamente");
        }
    }
    
}
