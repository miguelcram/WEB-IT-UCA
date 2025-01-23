package es.uca.iw.webituca.Views;

import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Model.Usuario;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uca.iw.webituca.Service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Editar Perfil")
@Route("/editar-perfil")
@RolesAllowed({"USUARIO", "OTP", "ADMIN", "CIO", "AVALADOR"})
public class UsuarioEditView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private final AuthenticatedUser authenticatedUser;
    private Usuario usuarioActual;

    @Autowired
    public UsuarioEditView(UsuarioService usuarioService, AuthenticatedUser authenticatedUser) {
        this.usuarioService = usuarioService;
        this.authenticatedUser = authenticatedUser;
        usuarioActual = authenticatedUser.get().get();

        if (usuarioActual == null) {
            Notification.show("No se pudo cargar el usuario actual.", 3000, Notification.Position.MIDDLE);
            return;
        }

        crearVista();
    }

    private void crearVista() {
        H1 titulo = new H1("Editar Perfil");
        getContent().add(titulo);

        FormLayout formulario = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        nombreField.setValue(usuarioActual.getNombre());

        TextField apellido1Field = new TextField("Primer Apellido");
        apellido1Field.setValue(usuarioActual.getApellido1());

        TextField apellido2Field = new TextField("Segundo Apellido");
        apellido2Field.setValue(usuarioActual.getApellido2());

        EmailField emailField = new EmailField("Correo Electrónico");
        emailField.setValue(usuarioActual.getEmail());

        TextField telefonoField = new TextField("Teléfono");
        telefonoField.setValue(usuarioActual.getTelefono());

        formulario.add(nombreField, apellido1Field, apellido2Field, emailField, telefonoField);

        // Botones para guardar o cancelar
        Button guardarBtn = new Button("Guardar", event -> {
            usuarioActual.setNombre(nombreField.getValue());
            usuarioActual.setApellido1(apellido1Field.getValue());
            usuarioActual.setApellido2(apellido2Field.getValue());
            usuarioActual.setEmail(emailField.getValue());
            usuarioActual.setTelefono(telefonoField.getValue());

            usuarioService.saveOrUpdateUsuario(usuarioActual); // Actualiza en la base de datos
            Notification.show("Cambios guardados con éxito.", 3000, Notification.Position.MIDDLE);
        });

        Button cancelarBtn = new Button("Cancelar", event -> {
            Notification.show("No se realizaron cambios.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("home");
        });

        HorizontalLayout botones = new HorizontalLayout(guardarBtn, cancelarBtn);
        botones.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);

        getContent().add(formulario, botones);
    }
}
