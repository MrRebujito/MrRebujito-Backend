package mrRebujito.MrRebujito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Producto;
import mrRebujito.MrRebujito.entity.Socio;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer>{
	
	//Consulta para obtener todas las casetas que están asociadas al socio 
	@Query("SELECT c FROM Caseta c JOIN c.socios s WHERE s.id = ?1")
	List<Caseta> findCasetasBySocioId(int socioId);
	
    
    //Listar los productos de una caseta en especifico
    @Query("SELECT p FROM Caseta c JOIN c.productos p WHERE c.id = ?1")
    List<Producto> findProductosByCasetaId(int casetaId);
    
}