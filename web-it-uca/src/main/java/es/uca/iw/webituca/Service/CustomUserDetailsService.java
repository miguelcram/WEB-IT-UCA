package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.repositories.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                //.roles(usuario.getRol()) // Role (e.g., ROLE_USER)
                .build();
    }
}
