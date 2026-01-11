package mrRebujito.MrRebujito.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
	public Optional<Administrador> findByUsername(String username);
}
