package es.uca.iw.webituca.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.UI;

@Route(value = "/login")
public class LoginView extends Composite<VerticalLayout> {
    public LoginView() {
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            if (authenticate(username, password)) {
                UI.getCurrent().navigate("/home");
            } else {
                Notification.show("Invalid credentials");
            }
        });

        getContent().add(new Span("Login"));
        getContent().add(usernameField);
        getContent().add(passwordField);
        getContent().add(loginButton);
    }

    private boolean authenticate(String username, String password) {
        // Aquí iría la lógica para autenticar al usuario con la base de datos
        // Por simplicidad, vamos a suponer que hay un usuario "user" con contraseña "pass"
        return "user".equals(username) && "pass".equals(password);
    }
}