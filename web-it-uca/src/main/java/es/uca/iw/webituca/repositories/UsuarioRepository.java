package es.uca.iw.webituca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.webituca.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
}
