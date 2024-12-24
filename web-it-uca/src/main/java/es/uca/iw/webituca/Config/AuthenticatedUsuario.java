package es.uca.iw.webituca.Config;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.UsuarioRepository;

import com.vaadin.flow.spring.security.AuthenticationContext;

import org.springframework.security.core.userdetails.UserDetails;
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

    // @Transactional
    // public Optional<Usuario> get() {
    //     return authenticationContext.getAuthenticatedUser(Usuario.class)
    //             .map(usuarioDetails -> usuarioRepository.findByUsuario(usuarioDetails.getUsername()).get());
    // }

//     @Transactional
//     public Optional<Usuario> get() {
//         return authenticationContext.getAuthenticatedUser(UserDetails.class)
//     .flatMap(userDetails -> usuarioRepository.findByUsuario(userDetails.getUsername()));
//     }


@Transactional
    public Optional<Usuario> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .flatMap(userDetails -> {
                Optional<Usuario> usuario = usuarioRepository.findByUsuario(userDetails.getUsername());
                // if (usuario.isPresent() && !usuario.get().isActivo()) {
                //     logout();
                //     throw new UserNotActiveException("El usuario no está activo.");
                // }
                return usuario;
    });}
    public void logout() {
        authenticationContext.logout();
    }
}