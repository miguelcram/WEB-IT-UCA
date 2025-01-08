package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.ProyectoService;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.webituca.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "proyecto/avalar/:proyectoId")
@RolesAllowed({"Avalador"})
public class AvalarProyectoView  extends Composite<VerticalLayout> {
    
    private final ProyectoService proyectoService;
    private final EmailService emailService;

    @Autowired
    public AvalarProyectoView(ProyectoService proyectoService, EmailService emailService, RouteParameters parameters) {
        this.proyectoService = proyectoService;
        this.emailService = emailService;

        try {
            String proyectoIdParam = parameters.get("proyectoId").orElse(null);

            if (proyectoIdParam != null) {
                Long proyectoId = Long.parseLong(proyectoIdParam);
                // Buscar el proyecto por su ID
                Proyecto proyecto = proyectoService.buscarProyectoPorId(proyectoId)
                        .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
                // Construir la vista
                construirVista(proyecto);
            } else {
                mostrarError("El ID del proyecto es requerido.");
            }
        } catch (NumberFormatException e) {
            mostrarError("El ID del proyecto no es válido.");
        } catch (Exception e) {
            mostrarError("Ocurrió un error al cargar el proyecto.");
        }
    }

    private void construirVista(Proyecto proyecto) {

        VerticalLayout layout = getContent();
        layout.setSizeFull();

        // Mostrar los datos del proyecto
        layout.add(new Span("Datos del Proyecto:"));
        layout.add(new Span("Título: " + proyecto.getTitulo()));
        layout.add(new Span("Descripción: " + proyecto.getDescripcion()));

        // Pregunta de avalar
        layout.add(new Span("¿Estás conforme con avalar el siguiente proyecto?"));

        // Botón para aceptar (Avala el proyecto)
        Button aceptarButton = new Button("Aceptar", event -> {
            try {
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.EN_TRAMITE_AVALADO);
                enviarNotificacionEmail(proyecto);
                Notification.show("El proyecto ha sido avalado.");
                getUI().ifPresent(ui -> ui.navigate("home"));
            } catch(Exception e) {
                mostrarError("Error al avalar el proyecto.");
            }
        });

        // Botón para cancelar (Cancela el proyecto)
        Button cancelarButton = new Button("Cancelar", event -> {
            try {
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.RECHAZADO);
                enviarNotificacionEmail(proyecto);
                Notification.show("El proyecto ha sido rechazado.");
                getUI().ifPresent(ui -> ui.navigate("home"));
            } catch(Exception e) {
                mostrarError("Error al avalar el proyecto.");
            }
        });

        layout.add(aceptarButton, cancelarButton);
    }

    private void enviarNotificacionEmail(Proyecto proyecto) {
        String emailCreador = proyecto.getUsuario().getEmail();
        if (emailCreador != null && !emailCreador.isEmpty()) {
            String asunto = "Estado de tu proyecto actualizado";
            String estado = proyecto.getEstado().equals(Estado.EN_TRAMITE_AVALADO) ? "AVALADO" : "RECHAZADO";
            String cuerpo = String.format("El proyecto '%s' ha sido %s. Accede a la web para consultar tus proyectos.", 
                    proyecto.getTitulo(), estado);
            emailService.enviarEmail(emailCreador, asunto, cuerpo);
        }
    }

    private void mostrarError(String mensaje) {
        VerticalLayout layout = getContent();
        layout.removeAll();

        layout.add(new Span(mensaje));
        Button volverHome = new Button("Volver a Inicio", event -> getUI().ifPresent(ui -> ui.navigate("home")));
        layout.add(volverHome);
    }
}
