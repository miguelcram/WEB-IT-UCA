package es.uca.iw.webituca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uca.iw.webituca.Model.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
}
