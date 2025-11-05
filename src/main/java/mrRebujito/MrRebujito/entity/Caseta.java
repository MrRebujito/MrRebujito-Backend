package mrRebujito.MrRebujito.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Caseta extends Actor{
	// Atributos de la tabla Caseta
	@NotBlank
	@Column(unique = true)
	private String razonS;
	
	@NotNull
	@Min(0)
	private int aforo;
	
	@NotNull
	private boolean publica;
	
	@ManyToMany()
	private List<Socio> socios;
	
	@OneToMany()
	@JoinColumn(name = "caseta_id")
	private List<SolicitudLicencia> solicitudesLicencia;
	
	@ManyToMany
	private List<Producto> productos; 

	// Creación del constructor vacío y constructor completo
	public Caseta() {
		super();
	}

	public Caseta(@NotBlank String razonS, @NotBlank @Min(0) int aforo, @NotNull boolean publica) {
		super();
		this.razonS = razonS;
		this.aforo = aforo;
		this.publica = publica;
	}

	// Generamos los get y set para cada atributo
	public String getRazonS() {
		return razonS;
	}

	public void setRazonS(String razonS) {
		this.razonS = razonS;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	public boolean isPublica() {
		return publica;
	}

	public void setPublica(boolean publica) {
		this.publica = publica;
	}

	public List<Socio> getListaSocios() {
		return socios;
	}

	public void setListaSocios(List<Socio> listaSocios) {
		this.socios = listaSocios;
	}

	public List<Socio> getSocios() {
		return socios;
	}

	public void setSocios(List<Socio> socios) {
		this.socios = socios;
	}

	public List<SolicitudLicencia> getSolicitudesLicencia() {
		return solicitudesLicencia;
	}

	public void setSolicitudesLicencia(List<SolicitudLicencia> solicitudesLicencia) {
		this.solicitudesLicencia = solicitudesLicencia;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	//Métodos auxiliares add y remove
	
	 public void addSolicitud(SolicitudLicencia solicitud) {
			solicitudesLicencia.add(solicitud);
	 }

	 public void removeSolicitud(SolicitudLicencia solicitud) {
	    	solicitudesLicencia.remove(solicitud);
	 }
	    
	 public void addSocio(Socio socio) {
			socios.add(socio);
	 }

	 public void removeSolicitud(Socio socio) {
	    	socios.remove(socio);
	 }
	 
	 public void addProducto(Producto producto) {
			productos.add(producto);
	 }

	 public void removeProducto(Producto producto) {
	    	productos.remove(producto);
	 }
	
	
	
	
	
}
