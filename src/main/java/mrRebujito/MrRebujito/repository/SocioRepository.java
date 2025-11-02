package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Socio;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer>{
	
	
	
}