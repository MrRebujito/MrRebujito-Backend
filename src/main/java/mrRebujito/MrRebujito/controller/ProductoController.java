package mrRebujito.MrRebujito.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.service.ProductoService;

@RestController
@RequestMapping("/producto")
@Tag(name = "Productos", description = "Gestión de la carta de productos (Solo Casetas pueden editar)")
public class ProductoController {

    @Autowired
    private ProductoService productoService;


    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Devuelve la carta completa de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay productos en la base de datos") 
    })
    public ResponseEntity<List<Producto>> findAll() {
        List<Producto> productos = productoService.findAllProducto();
        
        if (productos == null || productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Devuelve el detalle de un producto")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado") 
    })
    public ResponseEntity<Producto> findById(@PathVariable int id) {
        Optional<Producto> opProducto = productoService.findProductoById(id);

        return opProducto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    @Operation(summary = "Crear producto", description = "Requiere rol CASETA. Crea un nuevo producto.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (precio negativo o nombre vacío)"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso (No eres CASETA)") 
    })
    public ResponseEntity<?> save(@RequestBody Producto pro) {

        if (pro == null || pro.getNombre() == null || pro.getNombre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del producto es obligatorio.");
        }
        if (pro.getPrecio() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El precio no puede ser negativo.");
        }

        Producto nuevoProducto = productoService.saveProducto(pro);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Requiere rol CASETA. Modifica un producto existente.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso") 
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Producto pro) {

        if (pro == null || pro.getNombre() == null || pro.getNombre().isEmpty()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos para actualizar.");
        }

        Producto productoActualizado = productoService.updateProducto(id, pro);

        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el producto con id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Requiere rol CASETA. Elimina un producto.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "204", description = "Producto eliminado (No Content)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso") 
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (productoService.findProductoById(id).isPresent()) {
            productoService.deleteById(id);
           
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}