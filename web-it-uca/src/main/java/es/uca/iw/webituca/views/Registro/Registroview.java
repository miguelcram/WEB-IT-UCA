package es.uca.iw.webituca.Views.Registro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("registro")
@AnonymousAllowed
public class RegistroView extends VerticalLayout {

    @Autowired
    private UsuarioService usuarioService;

    private TextField nombre = new TextField("Nombre");
    private TextField apellido1 = new TextField("Primer Apellido");
    private TextField apellido2 = new TextField("Segundo Apellido");
    private EmailField email = new EmailField("Email");
    private TextField telefono = new TextField("Teléfono");
    private TextField usuarioField = new TextField("Usuario");
    private PasswordField password = new PasswordField("Contraseña");
    private PasswordField repitePassword = new PasswordField("Repite Contraseña");

    public RegistroView() {
        H1 h1 = new H1("Registro");
        FormLayout form = new FormLayout();
        form.add(nombre, apellido1, apellido2, email, telefono, usuarioField, password, repitePassword);

        Button guardarButton = new Button("Guardar", event -> {
            if (!password.getValue().equals(repitePassword.getValue())) {
                Notification.show("Las contraseñas no coinciden");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre.getValue());
            usuario.setApellido1(apellido1.getValue());
            usuario.setApellido2(apellido2.getValue());
            usuario.setEmail(email.getValue());
            usuario.setTelefono(telefono.getValue());
            usuario.setUsuario(usuarioField.getValue());
            usuario.setPassword(password.getValue());

            usuarioService.save(usuario);

            Notification.show("Usuario guardado en la base de datos");
        });

        add(h1, form, guardarButton);
    }
}