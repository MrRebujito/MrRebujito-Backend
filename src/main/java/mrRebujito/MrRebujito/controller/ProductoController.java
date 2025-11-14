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
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.service.ProductoService;

@RestController

@RequestMapping("/producto")

@Tag(name = "Productos", description = "Controlador para la gestión de los productos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping
	@Operation(summary = "Obtener todos los productos", description = "Te devuelve todos los productos que hay")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
			@ApiResponse(responseCode = "204", description = "No hay productos registrados") })

	// Método para devolver todos los productos
	public ResponseEntity<List<Producto>> findAll() {
		List<Producto> productos = productoService.findAll();

		if (productos != null && !productos.isEmpty()) {
			return ResponseEntity.ok(productos);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productos);
		}
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar producto por id", description = "Busca un producto específico utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Producto encontrado"),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content) })

	// Método que te devuelve un producto
	public ResponseEntity<Producto> findById(@PathVariable int id) {
		Optional<Producto> opProducto = productoService.findById(id);

		if (opProducto.isPresent()) {
			Producto producto = opProducto.get();
			return ResponseEntity.ok(producto);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo producto", description = "Registra un nuevo producto en la base de datos")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
			@ApiResponse(responseCode = "400", description = "Datos del producto inválidos") })
	// Método para guardar producto
	public ResponseEntity<String> save(@RequestBody Producto pro) {
		
		if (pro == null || pro.getNombre() == null || pro.getNombre().isEmpty() || pro.getTipoAlimento() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del producto inválidos");
		}

		productoService.save(pro);
		return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado correctamente");

	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un producto", description = "Actualiza la información de un producto existente según su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado") })
	// Método para actualizar producto
	public ResponseEntity<String> update(@PathVariable int id, @RequestBody Producto pro) {

		if (pro == null || pro.getNombre() == null || pro.getNombre().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del producto inválidos");
		}

		if (productoService.update(id, pro) == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");

		} else {

			return ResponseEntity.status(HttpStatus.OK).body("Producto actualizado correctamente");
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un producto", description = "Elimina un producto existente de la base de datos utilizando su id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
			@ApiResponse(responseCode = "404", description = "Producto no encontrado") })

	// Método para borrar producto
	public ResponseEntity<String> delete(@PathVariable int id) {
		Optional<Producto> opProducto = productoService.findById(id);

		if (opProducto.isPresent()) {
			productoService.deleteById(id);

			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
		}
	}

}