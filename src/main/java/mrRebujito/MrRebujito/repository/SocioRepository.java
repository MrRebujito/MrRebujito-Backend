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
	
    //Consulta para listar todos los ayuntamientos
    @Query("SELECT a FROM Ayuntamiento a")
    List<Ayuntamiento> findAllAyuntamientos();
    
    //Listar todas las casetas junto a sus cartas. El el distinc se usa para evitar duplicados con el fetch,
    //el fetch es lo que permite sacar los productos de las casetas en la misma consulta
    @Query("SELECT DISTINCT c FROM Caseta c LEFT JOIN FETCH c.productos")
    List<Caseta> findAllCasetasConProductos();
    
    //Listar los productos de una caseta en especifico
    @Query("SELECT p FROM Caseta c JOIN c.productos p WHERE c.id = ?1")
    List<Producto> findProductosByCasetaId(int casetaId);
    
}