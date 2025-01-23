package es.uca.iw.webituca.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        //Generar codigo de registro y email para la activacion
        if(!usuario.isEnabled()) {
            usuario.setCodigoRegistro(UUID.randomUUID().toString());
            usuario.setActivo(false);
            emailService.enviarCorreoRegistro(usuario);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return savedUsuario;
    }

    public Optional<Usuario> findByUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

        // Convierte roles a una lista de autoridades
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRol().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(), 
                true,
                true,
                true,
                authorities
        );
    }

    //Activar usuario
    public boolean activarUsuario(String email, String codigoRegistro) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if(usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if(usuario.getCodigoRegistro() != null && usuario.getCodigoRegistro().equals(codigoRegistro)) {
                usuario.setActivo(true);
                usuario.setCodigoRegistro(null);
                usuario.setRol(Rol.USUARIO);
                usuarioRepository.save(usuario);
                System.out.println("Usuario activado con email: " + email);
                return true;    //Usuario activado
            } else {
                System.out.println("Código de activación incorrecto para: " + email);
            }
        } else {
            System.out.println("No se encontró un usuario con el email: " + email);
        }
        return false;   //Si ocurre cualquier problemas, no se activa
    }

    //Método count() para uso en base de datos
    public long count() {
        return usuarioRepository.count();
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario saveOrUpdateUsuario(Usuario usuario) {
    return usuarioRepository.save(usuario);
    }

   
    public void deleteUsuario(Long id) {
    try {
        usuarioRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new IllegalStateException("El usuario no puede ser eliminado porque tiene proyectos asociados.", e);
    }
}

    public Usuario updateRol(Long usuarioId, Rol nuevoRol) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setRol(nuevoRol);  // Cambiar el rol
            return usuarioRepository.save(usuario);  // Guardar la actualización
        }
        return null;  // Devolver null si el usuario no se encuentra
    }

    public List<Usuario> getAvaladores() {
        return usuarioRepository.findByRol(Rol.AVALADOR);
    }
    
}