package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Caseta;
import java.util.Optional;


@Repository
public interface CasetaRepository extends JpaRepository<Caseta, Integer> {
	// Método para buscar por username
	public Optional<Caseta> findByUsername(String username);
	
	//Metodo para buscar casetas por socio
}
