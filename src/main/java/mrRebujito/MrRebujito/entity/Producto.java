package mrRebujito.MrRebujito.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Producto extends DomainEntity {

		//Declaración de atributos, 
		@NotNull
		private String Nombre;
		
		@Enumerated(EnumType.STRING)
		private TipoAlimento tipoAlimento;
		
		private Double precio;
		
		//Declaramos este atributo para la relacción ManyToOne con Caseta
		@ManyToOne
	    @JoinColumn(name = "caseta_id")
	    private Caseta caseta;
		
		

		public Producto() {
			super();
		}

		//Generamos los Getters y los Setters
		public String getNombre() {
			return Nombre;
		}

		public void setNombre(String nombre) {
			Nombre = nombre;
		}

		public TipoAlimento getTipoAlimento() {
			return tipoAlimento;
		}

		public void setTipoAlimento(TipoAlimento tipoAlimento) {
			this.tipoAlimento = tipoAlimento;
		}

		public Double getPrecio() {
			return precio;
		}

		public void setPrecio(Double precio) {
			this.precio = precio;
		}

		public Caseta getCaseta() {
			return caseta;
		}

		public void setCaseta(Caseta caseta) {
			this.caseta = caseta;
		}
		
		
}
