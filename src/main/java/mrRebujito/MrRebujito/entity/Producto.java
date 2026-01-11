package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Producto extends DomainEntity {

		//Declaración de atributos, 
		@NotBlank
		private String nombre;
		
		@NotNull
		@Enumerated(EnumType.STRING)
		private TipoAlimento tipoAlimento;
		
		@Min(value = 0)
		private double precio;
		
		public Producto() {
			super();
		}

		//Generamos los Getters y los Setters
		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public TipoAlimento getTipoAlimento() {
			return tipoAlimento;
		}

		public void setTipoAlimento(TipoAlimento tipoAlimento) {
			this.tipoAlimento = tipoAlimento;
		}

		public double getPrecio() {
			return precio;
		}

		public void setPrecio(Double precio) {
			this.precio = precio;
		}
		
}
