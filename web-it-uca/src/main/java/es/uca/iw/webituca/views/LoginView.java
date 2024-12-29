package es.uca.iw.webituca.Views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.UI;
import es.uca.iw.webituca.Service.UsuarioService;
import es.uca.iw.webituca.Model.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "login")
@AnonymousAllowed
public class LoginView extends Composite<VerticalLayout> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginView() {
        TextField usernameField = new TextField("Usuario");
        PasswordField passwordField = new PasswordField("Contraseña");
        Button loginButton = new Button("Identificate", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            if (authenticate(username, password)) {
                VaadinSession.getCurrent().setAttribute("user", username);
                UI.getCurrent().navigate("home");
            } else {
                Notification.show("Error en el inicio de sesión. Compruebe su usuario, contraseña o si su cuenta está activada.");
            }
        });

        Button registro = new Button("Registro", event -> {
            UI.getCurrent().navigate("registro");
        });

        getContent().add(new Span("Login"));
        getContent().add(usernameField);
        getContent().add(passwordField);
        getContent().add(loginButton);
        getContent().add(registro);
    }

    private boolean authenticate(String usuario, String password) {
        Optional<Usuario> usuarioOpt = usuarioService.findByUsuario(usuario);
        if(usuarioOpt.isPresent()) {
            Usuario user = usuarioOpt.get();
            if(passwordEncoder.matches(password, user.getPassword())) {
                //Comprobar si el usuario esta activo
                if(user.isEnabled()) {
                    return true;
                } else {
                    Notification.show("La cuenta no está activada. Revisa tu correo para activar tu cuenta.");
                }
            } else {
                Notification.show("Contraseña incorrecta.");
            }
        } else {
            Notification.show("Usuario no encontrado.");
        }
        return false;
    }
}