package mrRebujito.MrRebujito.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.security.JWTUtils;
import mrRebujito.MrRebujito.service.CasetaService;

@RestController
@RequestMapping("/caseta")
@Tag(name = "Casetas", description = "Controlador para la gestión de casetas")
public class CasetaController {

    @Autowired
    private CasetaService casetaService;
    
    @Autowired
    private JWTUtils jwtUtils;
    

    @PostMapping("/registrar")
    @Operation(summary = "Registrar nueva caseta", description = "Crea una nueva cuenta de caseta.")
    public ResponseEntity<?> registrarCaseta(@RequestBody Caseta caseta) {
        String foto = caseta.getFoto();
        if (foto != null && !foto.matches("^(https?):/?/?[^.]+\\.[^.]+\\.[^.]+$")) {
            return ResponseEntity.badRequest().body("La foto debe ser un enlace válido");
        }
        
        try {
            Caseta nuevaCaseta = casetaService.save(caseta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCaseta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la caseta");
        }
    }
    
    @GetMapping("/me")
    @Operation(summary = "Ver mi perfil", description = "Devuelve los datos de la caseta logueada.")
    public ResponseEntity<Caseta> verMiPerfil() {
        Object actor = jwtUtils.userLogin();
        if (actor instanceof Caseta) {
            return ResponseEntity.ok((Caseta) actor);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @PutMapping("/editar/{id}")
    @Operation(summary = "Editar mi caseta", description = "Actualiza los datos si eres el dueño.")
    public ResponseEntity<?> editarCaseta(@PathVariable Integer id, @RequestBody Caseta casetaEditada) {
        Optional<Caseta> casetaExistente = casetaService.findById(id);

        if (casetaExistente.isPresent()) {
            Object actorLogueado = jwtUtils.userLogin();
            
            // SEGURIDAD: Comprobamos que el que edita es el dueño de la caseta
            if (actorLogueado instanceof Caseta && ((Caseta) actorLogueado).getId() == id) {
                
                // Validación de aforo (lógica del update del servicio)
                Caseta guardada = casetaService.update(id, casetaEditada);
                
                if (guardada == null) {
                   return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El aforo no puede ser menor que los socios actuales.");
                }
                return ResponseEntity.ok(guardada);
                
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para editar esta caseta.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar caseta", description = "Elimina la caseta si eres el dueño y no tiene socios.")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Caseta> oCaseta = casetaService.findById(id);

        if (oCaseta.isPresent()) {
            // SEGURIDAD: Añadida comprobación de dueño para borrar
            Object actorLogueado = jwtUtils.userLogin();
            if (!(actorLogueado instanceof Caseta) || ((Caseta) actorLogueado).getId() != id) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar esta caseta.");
            }

            Caseta caseta = oCaseta.get();
            int numSocios = (caseta.getListaSocios() != null) ? caseta.getListaSocios().size() : 0;

            if (numSocios > 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la caseta, tiene socios inscritos");
            } else {
                casetaService.delete(id);
                return ResponseEntity.ok("Caseta eliminada correctamente");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    @Operation(summary = "Listar todas", description = "Obtiene todas las casetas.")
    public ResponseEntity<List<Caseta>> listarCasetas() {
        List<Caseta> casetas = casetaService.findAll();
        if (casetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(casetas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene una caseta pública por su ID.")
    public ResponseEntity<Caseta> findById(@PathVariable int id) {
        return casetaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/{casetaId}/solicitud/{ayuntamientoId}")
    public ResponseEntity<String> crearSolicitud(@PathVariable int casetaId, @PathVariable int ayuntamientoId) {
        try {
            casetaService.crearSolicitud(casetaId, ayuntamientoId);
            return ResponseEntity.ok("Solicitud creada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{socioId}/anadirSocio/{casetaId}")
    public ResponseEntity<String> addSocio(@PathVariable int socioId, @PathVariable int casetaId) {
        try {
            casetaService.addSocio(casetaId, socioId);
            return ResponseEntity.ok("Socio añadido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("No se pudo añadir al socio.");
        }
    }

    @PostMapping("/{productoId}/anadirProducto/{casetaId}")
    public ResponseEntity<String> addProducto(@PathVariable int productoId, @PathVariable int casetaId) {
        try {
            casetaService.addProducto(casetaId, productoId);
            return ResponseEntity.ok("Producto añadido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("No se pudo añadir el producto.");
        }
    }
}