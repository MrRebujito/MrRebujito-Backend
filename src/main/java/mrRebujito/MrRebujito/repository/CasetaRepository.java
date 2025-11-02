package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Caseta;

@Repository
public interface CasetaRepository extends JpaRepository<Caseta, Integer> {

}
