package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
        layout.setSpacing(false);
        layout.setPadding(false);

        if (authenticatedUser.get().isEmpty()) {
            layout.add(new Span("Debe iniciar sesión para acceder a esta vista."));
            return;
        }

        // Obtener el usuario autenticado
        Usuario usuario = authenticatedUser.get().get();

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
        grid.addColumn(Proyecto::getTitulo).setHeader("Título").setAutoWidth(true);
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Proyecto::getFechaInicio).setHeader("Fecha Inicio").setAutoWidth(true);
        grid.addColumn(Proyecto::getFechaFin).setHeader("Fecha Fin").setAutoWidth(true);
        grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto").setAutoWidth(true);
        grid.addColumn(Proyecto::getPrioridad).setHeader("Prioridad").setAutoWidth(true);
        grid.addColumn(proyecto -> {
            Usuario solicitante = proyecto.getUsuario();
            return solicitante.getId() + " - " + solicitante.getNombre();
        }).setHeader("Solicitante").setAutoWidth(true);

        // Columna con botón de gestionar
        grid.addComponentColumn(proyecto -> {
            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            buttonsLayout.setSpacing(true);
            buttonsLayout.setPadding(true);

            // Campo para prioridad
            ComboBox<Integer> prioridadField = new ComboBox<>("Prioridad");
            prioridadField.setItems(1, 2, 3, 4, 5); // Prioridad de 1 (baja) a 5 (alta)
            prioridadField.setValue(proyecto.getPrioridad());
            prioridadField.setWidth("80px");

            prioridadField.addValueChangeListener(event -> {
                proyecto.setPrioridad(event.getValue());
            });
            
            //Boton de aceptar
            Button aceptarButton = new Button("Aceptar", event -> {
                if (prioridadField.getValue() == null) {
                    Notification.show("Debe seleccionar una prioridad antes de aceptar el proyecto.");
                    return;
                }
                
                proyecto.setPrioridad(prioridadField.getValue());
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.EN_TRAMITE_AVALADO);
                proyectoService.guardarProyecto(proyecto, null);
                enviarNotificacionEmail(proyecto, true);
                Notification.show("El proyecto ha sido avalado con prioridad " + prioridadField.getValue() + ".");
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            aceptarButton.setWidth("90px");

            //Boton de cancelar
            Button cancelarButton = new Button("Cancelar", event -> {
                proyectoService.cambiarEstadoProyecto(proyecto.getId(), Estado.RECHAZADO);
                enviarNotificacionEmail(proyecto, false);
                Notification.show("El proyecto ha sido rechazado.");
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            cancelarButton.setWidth("90px");
            
            buttonsLayout.add(prioridadField, aceptarButton, cancelarButton);
            return buttonsLayout;
        }).setHeader("Gestionar").setAutoWidth(true);

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
