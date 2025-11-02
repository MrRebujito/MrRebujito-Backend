package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>{

}
