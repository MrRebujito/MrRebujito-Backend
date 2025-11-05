package mrRebujito.MrRebujito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mrRebujito.MrRebujito.entity.SolicitudLicencia;

@Repository
public interface SolicitudLicenciaRepository extends JpaRepository<SolicitudLicencia, Integer> {
	/**
     * Cuenta el número de licencias que están en estado APROBADA para un Ayuntamiento específico.
     */
    @Query("SELECT COUNT(s) FROM SolicitudLicencia s " +
           "WHERE s.ayuntamiento.id = :ayuntamientoId AND s.estadoLicencia = 'APROBADA'")
    long countApprovedLicensesByAyuntamiento(@Param("ayuntamientoId") int ayuntamientoId);

}
