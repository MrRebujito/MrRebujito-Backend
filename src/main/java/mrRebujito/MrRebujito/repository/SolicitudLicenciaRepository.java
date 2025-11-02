package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.SolicitudLicencia;

@Repository
public interface SolicitudLicenciaRepository extends JpaRepository<SolicitudLicencia, Integer> {

}
