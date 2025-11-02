package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class SolicitudLicencia extends DomainEntity{
	// Atributo de tipo Enum (He creado un enum antes de esto)
	@NotNull
	private EstadoLicencia estadoLicencia;

	@ManyToOne
	private Ayuntamiento ayuntamiento;
	
	// Constructores, uno vacio y uno completo
	public SolicitudLicencia() {
		super();
	}

	public SolicitudLicencia(@NotNull EstadoLicencia estadoLicencia) {
		super();
		this.estadoLicencia = estadoLicencia;
	}

	// Generamos los get y set para el atributo de esta tabla
	public EstadoLicencia getEstadoLicencia() {
		return estadoLicencia;
	}

	public void setEstadoLicencia(EstadoLicencia estadoLicencia) {
		this.estadoLicencia = estadoLicencia;
	}
	
	
}
