package mrRebujito.MrRebujito.entity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Administrador extends Actor {
	
	  @NotBlank
	  private String primerApellido;

	 
	  private String segundoApellido;
	  
	  

	  public Administrador() {
		super();
	}

	  public String getPrimerApellido() {
		  return primerApellido;
	  }

	  public void setPrimerApellido(String primerApellido) {
		  this.primerApellido = primerApellido;
	  }

	  public String getSegundoApellido() {
		  return segundoApellido;
	  }

	  public void setSegundoApellido(String segundoApellido) {
		  this.segundoApellido = segundoApellido;
	  }
	  
	  

}
