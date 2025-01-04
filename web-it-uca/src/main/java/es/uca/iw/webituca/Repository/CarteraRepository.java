package es.uca.iw.webituca.Repository;

import es.uca.iw.webituca.Model.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteraRepository extends JpaRepository<Cartera, Long> {
  
}
