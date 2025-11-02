package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Socio extends Actor {

    @NotBlank
    private String primerApellido;

    @NotBlank
    private String segundoApellido;

    
    //Relación many to many con caseta
    @ManyToMany(mappedBy = "socios")
    private List<Caseta> casetas = new ArrayList<>();

    //Constructores 
    public Socio() {
        super();
    }

    public Socio(@NotBlank String nombre, String foto, @NotBlank String correo, String telefono, String direccion, 
                @NotBlank String primerApellido, @NotBlank String segundoApellido) {
        super(nombre, foto, correo, telefono, direccion);
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

    
    public List<Caseta> getCasetas() {
        return casetas;
    }

    
    public void setCasetas(List<Caseta> casetas) {
        this.casetas = casetas;
    }
}