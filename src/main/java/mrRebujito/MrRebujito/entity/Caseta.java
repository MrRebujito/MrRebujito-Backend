package mrRebujito.MrRebujito.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Caseta extends Actor{
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
	private List<SolicitudLicencia> solicitudesLicencia;
	
	@ManyToMany
	private List<Producto> productos; 

	// Creación del constructor vacío y constructor completo
	public Caseta() {
		super();
	}

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

	// Generamos los get y set para cada atributo
	
	
	
	
}
