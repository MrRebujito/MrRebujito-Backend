package mrRebujito.MrRebujito.entity;

import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Caseta  extends Actor{
	// Atributos de la tabla Caseta
	@NotBlank
	@Column(unique = true)
	private String razonS;
	
	@NotNull
	@Min(0)
	private int aforo;
	
	@NotNull
	private boolean publica;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(
			name = "Caseta_Socios", joinColumns = @JoinColumn(name = "caseta_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "socio_id", referencedColumnName = "id")
			)
	private List<Socio> socios;
	
	@OneToMany
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
	
	
	
}
