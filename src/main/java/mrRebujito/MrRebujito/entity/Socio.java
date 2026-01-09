package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Socio extends Actor {

    @NotBlank
    private String primerApellido;

    
    private String segundoApellido;

    

    //Constructores 
    public Socio() {
        super();
    }

    public Socio(String primerApellido, String segundoApellido) {
        super();
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
    }

    //Getters y setters 
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