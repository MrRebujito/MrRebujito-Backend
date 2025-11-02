package mrRebujito.MrRebujito.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
	
	private List<Socio> listaSocios;

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
		return listaSocios;
	}

	public void setListaSocios(List<Socio> listaSocios) {
		this.listaSocios = listaSocios;
	}
	
	
	
}
