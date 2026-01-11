package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.EstadoLicencia;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.entity.Roles;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.entity.SolicitudLicencia;
import mrRebujito.MrRebujito.repository.CasetaRepository;
import mrRebujito.MrRebujito.security.JWTUtils;

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
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTUtils JWTUtils;
	
	// Método para obtener una caseta por su ID.
	public Optional<Caseta> findCasetaById(int id) {
		return this.casetaRepository.findById(id);
	}
	
	public Optional<Caseta> findByUsername(String username) {
		return this.casetaRepository.findByUsername(username);
	}
	
	
	// Método para obtener todas las casetas
	public List<Caseta> findAllCaseta() {
		return this.casetaRepository.findAll();
	}
	
	public List<Socio> getAllSociosByCaseta() {
		List<Socio> res = null;
		Caseta caseta = JWTUtils.userLogin();
		if (caseta != null) {
			res = caseta.getSocios();
		}
		return res;
	}
	
	
	// Método para guardar una caseta
	public Caseta saveCaseta(Caseta caseta) {
		String passwordEncriptada = passwordEncoder.encode(caseta.getPassword());
		caseta.setPassword(passwordEncriptada);
		
		caseta.setRol(Roles.CASETA);
		
		return casetaRepository.save(caseta);
	}
	
	
	// Método para actualizar una caseta
	public Caseta updateCaseta(Caseta casetaDatos) {
		// Busco la caseta de la sesion
		Caseta caseta = JWTUtils.userLogin();
		
		// Compruebo si la caseta existe 
		if (caseta!= null) {
			
			//-- VALIDACIÓN AFORO DE LA CASETA --
			// Tenemos que contar cuantos socios tiene la caseta
			int numSocios = 0;
			
			// Comprobamos si la lista existe antes de accederla
			if (caseta.getSocios() != null) {
				numSocios = caseta.getSocios().size();
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
			
			return this.casetaRepository.save(caseta);
		}
		return null;
	}
	

	// Método para eliminar una caseta por id
	public boolean deleteCaseta() {
		Caseta caseta = JWTUtils.userLogin();
		if (caseta != null) {
			casetaRepository.deleteById(caseta.getId());
			return true;
		}
		return false;
	}
	
	//Métodos para las relaciones
	
	@Transactional
	public SolicitudLicencia crearSolicitud(int ayuntamientoId) {
	    
		Caseta caseta = JWTUtils.userLogin();
	    Optional<Ayuntamiento> ayuntamientoOptional = ayuntamientoService.findAyuntamientoById(ayuntamientoId);

	    // Validar existencia de Caseta y Ayuntamiento
	    if (caseta == null || ayuntamientoOptional.isEmpty()) {
	        return null; // No se puede crear la solicitud si alguno no existe
	    }

	    Ayuntamiento ayuntamiento = ayuntamientoOptional.get();

	    // 1. REGLA DE NEGOCIO: Validar Solicitud Pendiente/Activa
	    boolean solicitudActivaOPendiente = false;

	    for (SolicitudLicencia solicitudExistente : caseta.getSolicitudesLicencia()) {
	        if (solicitudExistente.getAyuntamiento().getId() == ayuntamientoId) {
	            EstadoLicencia estado = solicitudExistente.getEstadoLicencia();
	            if (estado == EstadoLicencia.PENDIENTE || estado == EstadoLicencia.APROBADA) {
	                solicitudActivaOPendiente = true;
	                break;
	            }
	        }
	    }

	    // Si ya existe una solicitud activa o pendiente, no creamos una nueva
	    if (solicitudActivaOPendiente) {
	        return null;
	    }

	    // 2. Creación y Guardado de la nueva Solicitud
	    SolicitudLicencia solicitud = new SolicitudLicencia();
	    solicitud.setAyuntamiento(ayuntamiento);
	    solicitud.setEstadoLicencia(EstadoLicencia.PENDIENTE);

	    caseta.getSolicitudesLicencia().add(solicitud);
	    this.casetaRepository.save(caseta);

	    return solicitud;
	}
	
	/**
     * Añade un Socio a una Caseta existente (Relación Unidireccional Caseta --> Socio).
     */
	@Transactional
	public Caseta addSocio(int socioId) {
		Caseta caseta = JWTUtils.userLogin();
	    Optional<Socio> socioOptional = socioService.findSocioById(socioId);

	    // 1. Validaciones de existencia
	    if (caseta == null) {
	        throw new RuntimeException("Caseta no encontrada.");
	    }
	    
	    if (socioOptional.isEmpty()) {
	        throw new RuntimeException("Socio con ID " + socioId + " no encontrado.");
	    }

	    Socio socio = socioOptional.get();

	    // 2. REGLA DE NEGOCIO 4: Validar el Aforo Máximo
	    // La caseta no puede registrar a más socios del aforo máximo.
	    
	    // Obtenemos el tamaño actual de la lista de socios, le sumamos uno ya que despúes queremos añadir un socio
	    int sociosActuales = caseta.getSocios().size() + 1;
	    int aforoMaximo = caseta.getAforo();
	    
	    // Si la cantidad de socios es igual o excede el aforo, lanzamos la excepción.
	    if (sociosActuales >= aforoMaximo) {
	        throw new IllegalStateException("Error de negocio: La caseta  ha alcanzado su aforo máximo (" + aforoMaximo + " socios).");
	    }
	    
	    // 3. Lógica de Adición

	    // Si el socio no está ya en la lista
	    if (!caseta.getSocios().contains(socio)) {
	        // Añadimos al nuevo socio
	        caseta.getSocios().add(socio);
	    } else {
	       throw new IllegalStateException("Error de negocio: El socio con ID " + socioId + " ya es miembro de la caseta.");
	    }

	    return casetaRepository.save(caseta);
	}
	
	/**
     * Elimina un Socio a una Caseta existente (Relación Unidireccional Caseta --> Socio).
     */
	@Transactional
	public Caseta removeSocio(int socioId) {
		Caseta caseta = JWTUtils.userLogin();
	    Optional<Socio> socioOptional = socioService.findSocioById(socioId);

	    // 1. Validaciones de existencia
	    if (caseta == null) {
	        throw new RuntimeException("Caseta no encontrada.");
	    }
	    
	    if (socioOptional.isEmpty()) {
	        throw new RuntimeException("Socio con ID " + socioId + " no encontrado.");
	    }

	    Socio socio = socioOptional.get();
	    
	    // 2. Lógica de Eliminacion

	    // El socio debe estar en la lista
	    if (caseta.getSocios().contains(socio)) {
	        // Añadimos al nuevo socio
	        caseta.getSocios().remove(socio);
	    } else {
	       throw new IllegalStateException("Error: El socio con ID " + socioId + " no es miembro de la caseta.");
	    }

	    return casetaRepository.save(caseta);
	}

    /**
     * Añade un Producto a una Caseta existente (Relación Unidireccional Caseta -> Producto).
     */
	public Caseta addProducto(int productoId) {
		Caseta caseta = JWTUtils.userLogin();
        Optional<Producto> productoOptional = productoService.findProductoById(productoId);
        
        if (caseta == null) {
            throw new RuntimeException("Caseta no encontrada.");
        }
        
        if (productoOptional.isEmpty()) {
            throw new RuntimeException("Socio con ID " + productoId + " no encontrado.");
        }
        
        Producto producto = productoOptional.get();
        
        if (!caseta.getSocios().contains(producto)) {
            caseta.getProductos().add(producto);
        }
        
        return casetaRepository.save(caseta);
	}
	
	 public List<Producto> getCartaCaseta() {
	        Caseta caseta = JWTUtils.userLogin();
	        if (caseta != null) {
	            return caseta.getProductos();
	        } else {
				return null;
			}
	   }
	 
	
}
