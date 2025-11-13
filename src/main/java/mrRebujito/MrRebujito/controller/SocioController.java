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
import mrRebujito.MrRebujito.entity.Producto;
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

	@GetMapping // sta anotación indica que este método responde a peticiones HTTP GET
	//Cuando alguien haga un GET a /socio, se ejecutará este método

	@Operation(summary = "Obtener todos los socios", description = "Te devuelve todos los socios que hay")
	
	//responseCode: "200" significa éxito (todo salió bien), se puede añadir máspara más respuestas
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista de socios obtenida exitosamente") })	
	
	//Método para devolver todos los socios 
	public ResponseEntity<List<Socio>> findAll() {
		
		List<Socio> socios = socioService.findAll();
		
		if (socios != null && !socios.isEmpty()) {
			
			return ResponseEntity.ok(socios);
			
		} else {
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(socios);
		}
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Buscar socio por id", description = "Busca un socio específico utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Socio encontrado"),
			@ApiResponse(responseCode = "400", description = "Socio no encontrado")
	})
	
	//Método que te devuelve un socio
	public ResponseEntity<Socio> findById(@PathVariable int id) {
	    Optional<Socio> opSocio = socioService.findById(id);
	    
	    //Si el Optional contiene un socio
	    if (opSocio.isPresent()) {
	    	
	        Socio socio = opSocio.get();  
	        
	        return ResponseEntity.ok(socio); //Devolvemos 200 OK con el socio
	    } 
	    //Si el Optional está vacío (no encontró el socio)
	    else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //Devolvemos 400 ERROR
	    }
	}
	
	
	@PostMapping //Peticiones HTTP POST
	@Operation(summary = "Crear un nuevo socio", description = "Registra un nuevo socio en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Socio creado correctamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el socio") 
	})
	//Método para guardar socio
	public ResponseEntity<String> save(@RequestBody Socio soc) {
		
		if (soc != null && soc.getNombre() != null && !soc.getNombre().isEmpty()) {
			
			socioService.save(soc);
			
			return ResponseEntity.status(HttpStatus.OK).body("Socio creado correctamente");
			
		} else {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del socio inválidos");
		}
	}
	
	
	@PutMapping("/{id}") //Peticiones HTTP PUT
	@Operation(summary = "Actualizar un socio", description = "Actualiza la información de un socio existente según su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Socio actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Socio no encontrado o datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el socio") 
	})
	public ResponseEntity<String> update(@PathVariable int id, @RequestBody Socio soc) {
		
		if (soc == null || soc.getNombre() == null || soc.getNombre().isEmpty()) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del socio inválidos");
		}
		
		if (socioService.update(id, soc) == null) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Socio no encontrado");
			
		} else {
			
			return ResponseEntity.status(HttpStatus.OK).body("Socio actualizado correctamente");
		}
	}
	
	@DeleteMapping("/{id}") //Peticiones HTTP DELETE
	@Operation(summary = "Eliminar un socio", description = "Elimina un socio existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Socio eliminado correctamente"),
			@ApiResponse(responseCode = "400", description = "Socio no encontrado") 
	})
	public ResponseEntity<String> delete(@PathVariable int id) {
		Optional<Socio> opSocio = socioService.findById(id);
		
		if (opSocio.isPresent()) {
			socioService.delete(id);
			
			return ResponseEntity.status(HttpStatus.OK).body("Socio eliminado correctamente");
			
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Socio no encontrado");
		}
	}
	
	//Requisitos funcionales
	@GetMapping("/{id}/casetas")
	@Operation(summary = "Obtener casetas del socio", description = "Lista y muestra las casetas a las que pertenece un socio")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Casetas obtenidas exitosamente"),
			@ApiResponse(responseCode = "404", description = "Socio no encontrado"),
			@ApiResponse(responseCode = "204", description = "El socio no pertenece a ninguna caseta")
	})
	public ResponseEntity<List<Caseta>> getCasetasBySocioId(@PathVariable int id) {
	    //Primero verificamos que el socio existe
	    Optional<Socio> opSocio = socioService.findById(id);
	    
	    if (!opSocio.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    
	    // Obtenemos las casetas del socio
	    List<Caseta> casetas = socioService.getCasetasBySocioId(id);
	    
	    if (casetas != null && !casetas.isEmpty()) {
	        return ResponseEntity.ok(casetas);
	    } else {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(casetas);
	    }
	}
	
	@GetMapping("/ayuntamientos")
	@Operation(summary = "Listar ayuntamientos", description = "Lista todos los ayuntamientos disponibles")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "Lista de ayuntamientos obtenida exitosamente"),
		@ApiResponse(responseCode = "204", description = "No hay ayuntamientos registrados")
	})
	public ResponseEntity<List<Ayuntamiento>> listarAyuntamientos() {
		List<Ayuntamiento> ayuntamientos = socioService.findAllAyuntamientos();
		
		if (ayuntamientos != null && !ayuntamientos.isEmpty()) {
			return ResponseEntity.ok(ayuntamientos);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ayuntamientos);
		}
	}
	
	@GetMapping("/casetas-con-carta")
	@Operation(summary = "Listar casetas con sus cartas", description = "Lista todas las casetas y sus productos (cartas)")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "Lista de casetas con cartas obtenida exitosamente"),
		@ApiResponse(responseCode = "204", description = "No hay casetas registradas")
	})
	public ResponseEntity<List<Caseta>> listarCasetasConCarta() {
		List<Caseta> casetas = socioService.findAllCasetasConProductos();
		
		if (casetas != null && !casetas.isEmpty()) {
			return ResponseEntity.ok(casetas);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(casetas);
		}
	}
	
	@GetMapping("/caseta/{casetaId}/productos")
	@Operation(summary = "Listar productos de una caseta", description = "Lista la carta (productos) de una caseta específica")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
		@ApiResponse(responseCode = "204", description = "La caseta no tiene productos"),
		@ApiResponse(responseCode = "404", description = "Caseta no encontrada")
	})
	public ResponseEntity<List<Producto>> listarProductosDeCaseta(@PathVariable int casetaId) {
		List<Caseta> todasCasetas = socioService.findAllCasetasConProductos();
		boolean casetaExiste = false;
		
		// Recorremos todas las casetas para verificar si existe la caseta con el ID proporcionado
		for (Caseta caseta : todasCasetas) {
			
			if (caseta.getId() == casetaId) {
				
				casetaExiste = true;
				break; //Se sale si se encuentra
			}
		}
		
		if (!casetaExiste) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		List<Producto> productos = socioService.findProductosByCasetaId(casetaId);
		
		if (productos != null && !productos.isEmpty()) {
			return ResponseEntity.ok(productos);
			
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productos);
		}
	}
}