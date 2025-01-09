package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Service.EmailService;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.webituca.Config.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "proyecto/avalar")
@RolesAllowed({"AVALADOR"})
public class AvalarProyectoView extends Composite<VerticalLayout> {
    
    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;
    private final EmailService emailService;

    @Autowired
    public AvalarProyectoView(ProyectoService proyectoService, AuthenticatedUser authenticatedUser, EmailService emailService) {
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        this.emailService = emailService;

        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setSizeFull();

        if (authenticatedUser.get().isEmpty()) {
            layout.add(new Span("Debe iniciar sesión para acceder a esta vista."));
            return;
        }

        // Obtener el usuario autenticado
        Usuario usuario = authenticatedUser.get().get();
        List<Proyecto> proyectos = proyectoService.listarProyectos();

        List<Long> avaladoresIds = proyectos.stream()
            .map(proyecto -> {
                Usuario avalador = proyecto.getAvalador();
                if (avalador != null) {
                    return avalador.getId();
                } else {
                    // Manejar el caso en el que avalador es null
                    return null;
                }
            })
            .filter(id -> id != null) // Filtrar los valores null
            .collect(Collectors.toList());


        // Filtramos proyectos por avalador y estado "EN_TRAMITE"
        List<Proyecto> proyectosPorAvalar = proyectoService.listarProyectos().stream().filter(proyecto -> {
            Usuario avalador = proyecto.getAvalador();
            return avalador != null && avalador.getId().equals(usuario.getId()) &&
                   proyecto.getEstado() == Estado.EN_TRAMITE;
            }).collect(Collectors.toList());
        
        if (proyectosPorAvalar.isEmpty()) {
            layout.add(new Span("No tiene proyectos por avalar en este momento."));
            return;
        }

        // Crear una Grid para mostrar los proyectos
        Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
        grid.setItems(proyectosPorAvalar);
        grid.addColumn(Proyecto::getTitulo).setHeader("Título");
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción");
        grid.addColumn(Proyecto::getFechaInicio).setHeader("Fecha Inicio");
        grid.addColumn(Proyecto::getFechaFin).setHeader("Fecha Fin");
        grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto");
        grid.addColumn(proyecto -> {
            Usuario solicitante = proyecto.getUsuario();
            return solicitante.getId() + " - " + solicitante.getNombre();
        }).setHeader("Solicitante");

        // Columna con botón de gestionar
        grid.addComponentColumn(proyecto -> {
            Button aceptarButton = new Button("Aceptar", event -> {
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.EN_TRAMITE_AVALADO);
                enviarNotificacionEmail(proyecto, true);
                Notification.show("El proyecto ha sido avalado.");
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            return aceptarButton;
        }).setHeader("Aceptar");
        
        grid.addComponentColumn(proyecto -> {
            Button cancelarButton = new Button("Cancelar", event -> {
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.RECHAZADO);
                enviarNotificacionEmail(proyecto, false);
                Notification.show("El proyecto ha sido rechazado.");
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            return cancelarButton;
        }).setHeader("Cancelar");

        layout.add(grid);
    }

    private void enviarNotificacionEmail(Proyecto proyecto, boolean avalado) {
        String emailCreador = proyecto.getUsuario().getEmail();
        if (emailCreador != null && !emailCreador.isEmpty()) {
            String asunto = "Estado de tu proyecto actualizado";
            String estado = avalado ? "AVALADO" : "RECHAZADO";
            System.out.println("Estado actual: " + proyecto.getEstado());
            System.out.println("Estado esperado: " + Estado.EN_TRAMITE_AVALADO);
            String cuerpo = String.format("El proyecto '%s' ha sido %s. Accede a la web para consultar tus proyectos.",
                    proyecto.getTitulo(), estado);
            emailService.enviarEmail(emailCreador, asunto, cuerpo);
        }
    }

}
