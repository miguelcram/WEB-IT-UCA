package es.uca.iw.webituca.Views;

// import com.vaadin.flow.component.Composite;
// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.html.Span;
// import com.vaadin.flow.component.notification.Notification;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.component.textfield.PasswordField;
// import com.vaadin.flow.component.textfield.TextField;
// import com.vaadin.flow.router.BeforeEnterEvent;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.server.VaadinSession;
// import com.vaadin.flow.server.auth.AnonymousAllowed;
// import com.vaadin.flow.component.UI;
import es.uca.iw.webituca.Service.UsuarioService;
import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Model.Usuario;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Route(value = "login")
// @AnonymousAllowed
// public class LoginView extends Composite<VerticalLayout> {

//     @Autowired
//     private UsuarioService usuarioService;

//     @Autowired
//     private PasswordEnoginView(AuthenticatedUser authenticatedUser) {
//       eld usernameField = new TextField("Usuario");
//         new Button("Identificate", event -> {
//             String username = usernameField.getValue();
//             String password = passwordField.getValue();
//             if (authenticate(username, password)) {
//                 VaadinSession.getCurrent().setAttribute("user", username);
//                 UI.getCurrent().navigate("home");
//             } else {
//                 Notification.show("Error en el inicio de sesión. Compruebe su usuario, contraseña o si su cuenta está activada.");
//             }
//         });

//         Button registro = new Button("Registro", event -> {
//             UI.getCurrent().navigate("registro");
//         });

//         getContent().add(new Span("Login"));
//         getContent().add(usernameField);
//         getContent().add(passwordField);
//         getContent().add(loginButton);
//         getContent().add(registro);
//     }

//     private boolean authenticate(String usuario, String password) {
//         Optional<Usuario> usuarioOpt = usuarioService.findByUsuario(usuario);
//         if(usuarioOpt.isPresent()) {
//             Usuario user = usuarioOpt.get();
//             if(passwordEncoder.matches(password, user.getPassword())) {
//                 //Comprobar si el usuario esta ac        }

//         Optional<Usuario> optionalUser = authenticatedUser.get();
//         if (optionalUser.isPresent()) {
//             event.forwardTo("home");
//         }
//     }
// }

import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;



@AnonymousAllowed
@PageTitle("Login")
@Route(value = "/login", layout = MainLayout.class)
public class LoginView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final LoginForm loginForm;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        // Crear el formulario de login
        loginForm = new LoginForm();

        // Configurar el endpoint para las credenciales
        loginForm.setAction("login");

        // Configurar internacionalización
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.getForm().setTitle("Iniciar sesión");
        loginI18n.getForm().setUsername("Usuario");
        loginI18n.getForm().setPassword("Contraseña");
        loginI18n.getForm().setSubmit("Iniciar sesión");
        //loginI18n.getForm().setForgotPassword("¿Olvidó su contraseña?");
        loginForm.setI18n(loginI18n);

        // Diseño principal
        VerticalLayout layout = new VerticalLayout(loginForm);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Botón adicional (registrarse)
        Button registroButton = new Button("Registrarse", e -> UI.getCurrent().navigate(RegistroView.class));
        HorizontalLayout botonLayout = new HorizontalLayout(registroButton);
        botonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        botonLayout.setWidthFull();

        // Añadir componentes al contenido principal
        getContent().add(layout, botonLayout);
        getContent().setSpacing(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // Muestra mensaje de error si la autenticación falla
        }

        Optional<Usuario> optionalUser = authenticatedUser.get();
        if (optionalUser.isPresent()) {
            event.forwardTo("home");
        }
    }
}