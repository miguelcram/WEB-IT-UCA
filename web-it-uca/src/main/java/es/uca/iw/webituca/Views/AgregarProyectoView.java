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

import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.webituca.Model.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "nuevo_proyecto")
@RolesAllowed("Usuario")
public class AgregarProyectoView extends Composite<VerticalLayout> {

    private ProyectoService proyectoService;
    
    private UsuarioService usuarioService;

    @Autowired

    public AgregarProyectoView() {
        // Crear elementos del formulario
        TextField nombreProyecto = new TextField("Nombre del proyecto");
        TextField descripcion = new TextField("Descripción");
        Button guardarButton = new Button("Guardar");

        // Configurar acción del botón
        guardarButton.addClickListener(event -> {
            Usuario usuarioActual = getUsuarioActual();

            if (usuarioActual != null) {
                Usuario usuario = usuarioActual;

                // Guardar el proyecto con el usuario actual como dueño
                proyectoService.guardarProyecto(nombreProyecto.getValue(), descripcion.getValue(), usuario);
 
                Notification.show("Proyecto guardado exitosamente");
            } else {
                Notification.show("No se pudo identificar al usuario actual", 3000, Notification.Position.MIDDLE);
            }
        });

        // Agregar elementos al layout
        getContent().add(
                new Span("Agregar un nuevo proyecto"),
                nombreProyecto,
                descripcion,
                guardarButton
        );
    }

    private Usuario getUsuarioActual() {
        return ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class));
    }
}
