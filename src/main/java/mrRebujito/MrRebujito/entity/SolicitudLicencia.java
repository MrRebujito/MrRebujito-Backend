package mrRebujito.MrRebujito.entity;

import jakarta.validation.constraints.NotNull;

public class SolicitudLicencia extends DomainEntity{
	// Atributo de tipo Enum (He creado un enum antes de esto)
	@NotNull
	private EstadoLicencia estadoLicencia;

	
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
