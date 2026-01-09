package mrRebujito.MrRebujito.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Ayuntamiento;

@Repository
public interface AyuntamientoRepository extends JpaRepository<Ayuntamiento, Integer>{
	@Query("SELECT a FROM Ayuntamiento a WHERE a.username=?1")
	Optional<Ayuntamiento> findByUsername(String username);
}
