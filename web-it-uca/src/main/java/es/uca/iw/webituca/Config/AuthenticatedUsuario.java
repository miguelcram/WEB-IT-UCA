package es.uca.iw.webituca.Config;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.UsuarioRepository;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.security.AuthenticationContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUsuario {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUsuario(AuthenticationContext authenticationContext, UsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<Usuario> get() {
        System.out.println("AuthenticatedUsuario.get");
        Notification.show("AuthenticatedUsuario.get");
        return authenticationContext.getAuthenticatedUser(Usuario.class)
                .map(usuarioDetails -> usuarioRepository.findByUsuario(usuarioDetails.getUsername()));


    }

    public void logout() {
        authenticationContext.logout();
    }

}