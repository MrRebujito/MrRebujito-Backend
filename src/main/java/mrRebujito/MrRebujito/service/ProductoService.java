package mrRebujito.MrRebujito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.repository.ProductoRepository;
import mrRebujito.MrRebujito.security.JWTUtils;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	


	// Método para obtener los Productos
	public List<Producto> findAllProducto() {
		return this.productoRepository.findAll();
	}
	
	 //Metodo que devuelve el producto metiendole su id
	 public Optional<Producto> findProductoById(int id) {
	        return productoRepository.findById(id);
	 }
	
	//Método para guardar un producto introduciendole el objeto
	 public Producto saveProducto(Producto producto) {
	        return productoRepository.save(producto);
	 }
	 
	 public Producto updateProducto(int idProducto, Producto producto) {
			
			//Variable Producto optional para encontrar el producto por id
			Optional<Producto> opProducto = findProductoById(idProducto);
			
			//Comprueba si el socio existe
			if(opProducto.isPresent()) {
				
				//Si existe te guarda el producto y se hacen los cambios
				Producto pro = opProducto.get();
				
				pro.setNombre(producto.getNombre());
				pro.setPrecio(producto.getPrecio());
				pro.setTipoAlimento(producto.getTipoAlimento());
				
				//Te devuelve el producto guardado
				return saveProducto(pro);
			}
			
			//Si no existe te devuelve null
			return null;
			
		}

	 //Metodo que borra el producto metiendole su id
	 public void deleteById(int id) {
	        productoRepository.deleteById(id);
	    }
	
}
