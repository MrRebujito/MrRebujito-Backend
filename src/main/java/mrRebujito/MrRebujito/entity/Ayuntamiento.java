package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ayuntamiento extends Actor{
	
	@NotNull
	@Min(0)
	private int licenciaMax;

	
	public Ayuntamiento() {
		super();
	}

	public Ayuntamiento(@NotNull @Min(0) int licenciaMax) {
		super();
		licenciaMax = licenciaMax;
	}

	public int getLicenciaMax() {
		return licenciaMax;
	}

	public void setLicenciaMax(int licenciaMax) {
		licenciaMax = licenciaMax;
	}

}
