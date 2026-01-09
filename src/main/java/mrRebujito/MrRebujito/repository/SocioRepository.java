package mrRebujito.MrRebujito.repository;

import java.util.List;
import java.util.Optional;

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
	
    @Query("SELECT s FROM Socio s WHERE s.username = ?1")
    Optional<Socio> findByUsername(String username);
   
    
}