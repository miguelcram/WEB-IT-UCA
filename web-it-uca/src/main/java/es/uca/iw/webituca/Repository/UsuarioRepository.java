package es.uca.iw.webituca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uca.iw.webituca.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsuario(String usuario);
}
