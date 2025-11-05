package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ayuntamiento extends Actor{
	
	//Atributos de la tabla ayuntamiento
	@NotNull
	@Min(0)
	private int LicenciaMax;

	
	public Ayuntamiento() {
		super();
	}
	
	

	public Ayuntamiento(@NotNull @Min(0) int licenciaMax) {
		super();
		LicenciaMax = licenciaMax;
	}



	public int getLicenciaMax() {
		return LicenciaMax;
	}

	public void setLicenciaMax(int licenciaMax) {
		LicenciaMax = licenciaMax;
	}
	
	
	

}
