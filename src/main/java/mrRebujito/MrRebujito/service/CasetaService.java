package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.repository.CasetaRepository;


@Service
public class CasetaService {
	// Inyectamos la instancia de la clase CasetaRepository 
	@Autowired
	private CasetaRepository casetaRepository;
	
	@Autowired
    private AyuntamientoService ayuntamientoService;
	
	@Autowired
    private SocioService socioService;
	
	@Autowired
    private ProductoService productoService;
	
	// Método para obtener una caseta por su ID.
	public Optional<Caseta> findById(int id) {
		return this.casetaRepository.findById(id);
	}
	
	
	// Método para obtener todas las casetas
	public List<Caseta> findAll() {
		return this.casetaRepository.findAll();
	}
	
	
	// Método para guardar una caseta
	public Caseta save(Caseta caseta) {
		return casetaRepository.save(caseta);
	}
	
	
	// Método para actualizar una caseta
	public Caseta update(int id, Caseta casetaDatos) {
		// Busco la caseta por su id y la guardo en el Optional
		Optional<Caseta> oCaseta = findById(id);
		
		// Compruebo si la caseta existe 
		if (oCaseta.isPresent()) {
			// En caso de que exista obtengo el objeto
			Caseta caseta = oCaseta.get();
			
			//-- VALIDACIÓN AFORO DE LA CASETA --
			// Tenemos que contar cuantos socios tiene la caseta
			int numSocios = 0;
			
			// Comprobamos si la lista existe antes de accederla
			if (caseta.getListaSocios() != null) {
				numSocios = caseta.getListaSocios().size();
			}
			
			// Compruebo si el nuevo aforo que tenemos es menor que los socios actuales
			if (casetaDatos.getAforo() < numSocios) {
				// Si es menor, hacemos que no se actualice
				return null;
			}
			
			
			// Actualizo los campos que heredamos de Actor
			caseta.setNombre(casetaDatos.getNombre());
			caseta.setCorreo(casetaDatos.getCorreo());
			caseta.setTelefono(casetaDatos.getTelefono());
			caseta.setDireccion(casetaDatos.getDireccion());
			caseta.setFoto(casetaDatos.getFoto());
			
			// Actualizo los campos de Caseta
			caseta.setRazonS(casetaDatos.getRazonS());
			caseta.setAforo(casetaDatos.getAforo());
			caseta.setPublica(casetaDatos.isPublica());
			
			return save(caseta);
		}
		return null;
	}
	

	// Método para eliminar una caseta por id
	public void delete(int id) {
		this.casetaRepository.deleteById(id);
	}
	
	//Métodos para las relaciones
	
	@Transactional // Aseguramos que la carga de la lista y el guardado son atómicos
    public SolicitudLicencia crearSolicitud(int casetaId, int ayuntamientoId) {
        
        Optional<Caseta> casetaOptional = casetaRepository.findById(casetaId);
        Optional<Ayuntamiento> ayuntamientoOptional = ayuntamientoService.findById(ayuntamientoId);

        if (casetaOptional.isEmpty()) {
            throw new RuntimeException("Caseta con ID " + casetaId + " no encontrada.");
        }
        if (ayuntamientoOptional.isEmpty()) {
            throw new RuntimeException("Ayuntamiento con ID " + ayuntamientoId + " no encontrado.");
        }
        
        Caseta caseta = casetaOptional.get();
        Ayuntamiento ayuntamiento = ayuntamientoOptional.get();

        // 1. REGLA DE NEGOCIO: Validar Solicitud Pendiente/Activa
        
        // Comprobamos si la caseta ya tiene una solicitud (PENDIENTE o APROBADA) para este Ayuntamiento.
        boolean yaTieneSolicitudActivaOPendiente = caseta.getSolicitudesLicencia().stream()
            .anyMatch(s -> s.getAyuntamiento().getId() == ayuntamientoId && 
                           (s.getEstadoLicencia() == EstadoLicencia.PENDIENTE || 
                            s.getEstadoLicencia() == EstadoLicencia.APROBADA)); // Asegúrate de usar los nombres correctos del Enum

        if (yaTieneSolicitudActivaOPendiente) {
            String mensaje = String.format(
                "Error de negocio: La Caseta con ID %d ya tiene una solicitud pendiente o activa con el Ayuntamiento con ID %d.", 
                casetaId, ayuntamientoId);
            
            throw new IllegalStateException(mensaje);
        }
        
        // 2. Creación y Guardado de la Solicitud
        
        SolicitudLicencia solicitud = new SolicitudLicencia();
        solicitud.setAyuntamiento(ayuntamiento);
        solicitud.setEstadoLicencia(EstadoLicencia.PENDIENTE);

        // Al ser unidireccional, necesitamos primero guardar la Solicitud para obtener su ID

        // Añadir la solicitud a la lista de la Caseta (lado dueño de la relación)
        caseta.getSolicitudesLicencia().add(solicitud);
        
       casetaRepository.save(caseta);

        return solicitud;
    }
	
	/**
     * Añade un Socio a una Caseta existente (Relación Unidireccional Caseta --> Socio).
     */
	@Transactional
	public Caseta addSocio(int casetaId, int socioId) {
	    Optional<Caseta> casetaOptional = casetaRepository.findById(casetaId);
	    Optional<Socio> socioOptional = socioService.findById(socioId);

	    // 1. Validaciones de existencia
	    if (casetaOptional.isEmpty()) {
	        throw new RuntimeException("Caseta con ID " + casetaId + " no encontrada.");
	    }
	    
	    if (socioOptional.isEmpty()) {
	        throw new RuntimeException("Socio con ID " + socioId + " no encontrado.");
	    }

	    Caseta caseta = casetaOptional.get();
	    Socio socio = socioOptional.get();

	    // 2. REGLA DE NEGOCIO 4: Validar el Aforo Máximo
	    // La caseta no puede registrar a más socios del aforo máximo.
	    
	    // Obtenemos el tamaño actual de la lista de socios
	    int sociosActuales = caseta.getListaSocios().size();
	    int aforoMaximo = caseta.getAforo();
	    
	    // Si la cantidad de socios es igual o excede el aforo, lanzamos la excepción.
	    if (sociosActuales >= aforoMaximo) {
	        throw new IllegalStateException("Error de negocio: La caseta con ID " + casetaId + 
	                                       " ha alcanzado su aforo máximo (" + aforoMaximo + " socios).");
	    }
	    
	    // 3. Lógica de Adición

	    // Si el socio no está ya en la lista
	    if (!caseta.getListaSocios().contains(socio)) {
	        // Añadimos al nuevo socio
	        caseta.getListaSocios().add(socio);
	    } else {
	       throw new IllegalStateException("Error de negocio: El socio con ID " + socioId + " ya es miembro de la caseta " + casetaId + ".");
	    }

	    return casetaRepository.save(caseta);
	}

    /**
     * Añade un Producto a una Caseta existente (Relación Unidireccional Caseta -> Producto).
     */
	public Caseta addProducto(int casetaId, int productoId) {
		Optional<Caseta> casetaOptional = casetaRepository.findById(casetaId);
        Optional<Producto> productoOptional = productoService.findById(productoId);
        
        if (casetaOptional.isEmpty()) {
            throw new RuntimeException("Caseta con ID " + casetaId + " no encontrada.");
        }
        
        if (productoOptional.isEmpty()) {
            throw new RuntimeException("Socio con ID " + productoId + " no encontrado.");
        }
        
        Caseta caseta = casetaOptional.get();
        Producto producto = productoOptional.get();
        
        if (!caseta.getListaSocios().contains(producto)) {
            caseta.getProductos().add(producto);
        }
        
        return casetaRepository.save(caseta);
		
	}
}
