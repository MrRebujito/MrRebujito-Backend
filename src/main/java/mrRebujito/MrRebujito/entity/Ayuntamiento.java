package mrRebujito.MrRebujito.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Ayuntamiento extends Actor{
	
	//Atributos de la tabla ayuntamiento
	@NotNull
	@Min(0)
	private int LicenciaMax;

	public Ayuntamiento(int licenciaMax) {
		super();
		LicenciaMax = licenciaMax;
	}

	public Ayuntamiento() {
		super();
	}

	public int getLicenciaMax() {
		return LicenciaMax;
	}

	public void setLicenciaMax(int licenciaMax) {
		LicenciaMax = licenciaMax;
	}
	
	
	

}
