package es.uca.iw.webituca.Config;

import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.spring.security.AuthenticationContext;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.UsuarioRepository;

@Component
public class AuthenticatedUser {

    private final UsuarioRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(@Lazy AuthenticationContext authenticationContext, @Lazy UsuarioRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .flatMap(userDetails -> {
                Optional<Usuario> usuario = userRepository.findByUsuario(userDetails.getUsername());
                if (usuario.isPresent() && !usuario.get().isEnabled()) {
                    logout();
                    throw new UserNotActiveException("El usuario no estÃ¡ activo.");
                }
                return usuario;
            });
    }

    public void logout() {
        authenticationContext.logout();
    }

    public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message) {
        super(message);
    }
}
}