package es.uca.iw.webituca.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.UsuarioRepository;

@Service
public class UsuarioService {

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

    //Activar usuario
    public boolean activarUsuario(String email, String codigoRegistro) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if(usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if(usuario.getCodigoRegistro() != null && usuario.getCodigoRegistro().equals(codigoRegistro)) {
                usuario.setActivo(true);
                usuario.setCodigoRegistro(null);
                usuario.setRol(Rol.Usuario);
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
    usuarioRepository.deleteById(id);
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


}