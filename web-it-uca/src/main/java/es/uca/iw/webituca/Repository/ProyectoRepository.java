package es.uca.iw.webituca.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    //Optional<Proyecto> findByProyecto(String proyecto);
    //Optional<Proyecto> findByEmail(String email);
    List<Proyecto> findAll();
    List<Proyecto> findByUsuario(Usuario usuario);
}
