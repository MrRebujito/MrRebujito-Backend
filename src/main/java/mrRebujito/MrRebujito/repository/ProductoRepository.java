package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
}
