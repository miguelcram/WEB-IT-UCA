package es.uca.iw.webituca.Repository;

import es.uca.iw.webituca.Model.Cartera;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarteraRepository extends JpaRepository<Cartera, Long> {

    Optional<Cartera> findFirstByFechaCreacionLessThanEqualAndFechaFinGreaterThanEqual(LocalDateTime startDateTime, LocalDateTime endDateTime);
}

