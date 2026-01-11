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
	
	// Constructores, uno vacio y uno completo
	public SolicitudLicencia() {
		super();
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
	
}
