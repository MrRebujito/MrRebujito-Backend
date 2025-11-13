package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class SolicitudLicencia extends DomainEntity{
	// Atributo de tipo Enum (He creado un enum antes de esto)
	@NotNull
	@Enumerated(EnumType.STRING)
	private EstadoLicencia estadoLicencia;

	@ManyToOne
	private Ayuntamiento ayuntamiento;
	
	@ManyToOne
	private Caseta caseta;
	
	// Constructores, uno vacio y uno completo
	public SolicitudLicencia() {
		super();
	}


	public SolicitudLicencia(@NotNull EstadoLicencia estadoLicencia, Ayuntamiento ayuntamiento) {
		super();
		this.estadoLicencia = estadoLicencia;
		this.ayuntamiento = ayuntamiento;
	}



	// Generamos los get y set para el atributo de esta tabla
	public EstadoLicencia getEstadoLicencia() {
		return estadoLicencia;
	}

	public void setEstadoLicencia(EstadoLicencia estadoLicencia) {
		this.estadoLicencia = estadoLicencia;
	}
	
	public Ayuntamiento getAyuntamiento() {
		return ayuntamiento;
	}

	public void setAyuntamiento(Ayuntamiento ayuntamiento) {
		this.ayuntamiento = ayuntamiento;
	}
	
	public Caseta getCaseta() {
	    return caseta;
	}

	public void setCaseta(Caseta caseta) {
	    this.caseta = caseta;
	}
	
	
	
}
