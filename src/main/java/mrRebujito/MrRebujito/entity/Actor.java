package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Actor extends DomainEntity{
	
	@NotBlank
	private String nombre;
	
	private String foto;
	
	@NotBlank
	@Pattern(regexp = "^\\w[@]\\w[.]\\w$")
	private String correo;
	
	@Pattern(regexp = "^[6-9][0-9][8]$")
	private String telefono;
	
	private String direccion;

	public Actor() {
		super();
	}

	public Actor(@NotBlank String nombre, String foto, @NotBlank @Pattern(regexp = "^\\w[@]\\w[.]\\w$") String correo,
			@Pattern(regexp = "^[6-9][0-9][8]$") String telefono, String direccion) {
		super();
		this.nombre = nombre;
		this.foto = foto;
		this.correo = correo;
		this.telefono = telefono;
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
