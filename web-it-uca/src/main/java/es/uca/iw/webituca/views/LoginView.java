package es.uca.iw.webituca.views;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.UsuarioService;

@Route(value = "login", layout = MainLayout.class)
public class LoginView extends Composite<VerticalLayout> {

    @Autowired
    private UsuarioService usuarioService;

    public LoginView() {
        TextField usernameField = new TextField("Usuario");
        TextField emailField = new TextField("Email");
        getContent().add(emailField);

        PasswordField passwordField = new PasswordField("Contraseña");
        Button loginButton = new Button("Identificate", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            
            if (authenticate(username, password)) {
                VaadinSession.getCurrent().setAttribute("user", username);
                UI.getCurrent().navigate("home");
            } else {
                Notification.show("Error en el inicio de sesion. Compruebe su usuario y contraseña de nuevo.");
            }
        });

        Button registro = new Button("Registro", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String email = emailField.getValue();
            saveToDatabase(username, password, email);
        });

        getContent().add(new Span("Login"));
        getContent().add(usernameField);
        getContent().add(passwordField);
        getContent().add(loginButton);
        getContent().add(registro);
    }

    private boolean authenticate(String username, String password) {
        Optional<Usuario> userOptional = usuarioService.findByUsuario(username);
        if (userOptional.isPresent()) {
            Usuario user = userOptional.get();
            //Comprueba la contraseña (considerar hashing)
            return user.getPassword().equals(password);
        }
        return false;
    }

    private void saveToDatabase(String username, String password, String email) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setEmail(email);
        usuarioService.saveUsuario(usuario);
        Notification.show("Usuario registrado correctamente.");
    }
}
