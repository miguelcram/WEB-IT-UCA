package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Model.Usuario;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RolesAllowed({"AVALADOR", "OTP", "ADMIN", "USUARIO"})
@Route(value = "proyecto/ver-proyectos")
public class VerMisProyectosView extends VerticalLayout {

    private ProyectoService proyectoService;
    private AuthenticatedUser authenticatedUser;
    
    @Autowired
    public VerMisProyectosView(ProyectoService proyectoService, AuthenticatedUser authenticatedUser) {
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;

        if(authenticatedUser.get().isEmpty()) {
            Notification.show("Debes iniciar sesion para acceder a los proyectos.");
            return;
        }

        Usuario usuario = authenticatedUser.get().get();

        List<Proyecto> proyectos = proyectoService.listarProyectosPorUsuario(usuario);

        if(proyectos.isEmpty()) {
            Notification.show("No tienes proyectos registrados.");
            return;
        }

        Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
        grid.setItems(proyectos);
        grid.addColumn(Proyecto::getTitulo).setHeader("Título");
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción");
        grid.addColumn(Proyecto::getFechaInicio).setHeader("Fecha Inicio");
        grid.addColumn(Proyecto::getFechaFin).setHeader("Fecha Fin");
        grid.addColumn(Proyecto::getEstado).setHeader("Estado");
        grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto");

        // Columna de botones para editar/ver proyectos
        grid.addComponentColumn(proyecto -> {
            Button editarVerButton = new Button("Editar/Ver", click -> {
                editarProyecto(proyecto);
            });
            editarVerButton.setEnabled(proyecto.getEstado() != Estado.RECHAZADO && proyecto.getEstado() != Estado.TERMINADO);
            return editarVerButton;
        }).setHeader("Acción");

        add(grid);
    }

    private void editarProyecto(Proyecto proyecto) {
        removeAll(); // Limpiar la vista actual para mostrar el formulario de edición/visualización

        TextField tituloField = new TextField("Título");
        tituloField.setValue(proyecto.getTitulo());
        tituloField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        TextArea descripcionField = new TextArea("Descripción");
        descripcionField.setValue(proyecto.getDescripcion());
        descripcionField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        TextField presupuestoField = new TextField("Presupuesto");
        presupuestoField.setValue(String.valueOf(proyecto.getPresupuesto()));
        presupuestoField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        Button guardarButton = new Button("Guardar cambios", event -> {
            proyecto.setTitulo(tituloField.getValue());
            proyecto.setDescripcion(descripcionField.getValue());
            try {
                proyecto.setPresupuesto(Float.parseFloat(presupuestoField.getValue()));
            } catch (NumberFormatException e) {
                Notification.show("Presupuesto inválido. Debe ser un número con hasta 2 decimales.");
                return;
            }
            proyectoService.guardarProyecto(proyecto, null);
            Notification.show("Proyecto actualizado.");
            getUI().ifPresent(ui -> ui.navigate("home"));
        });

        guardarButton.setVisible(proyecto.getEstado() != Estado.RECHAZADO && proyecto.getEstado() != Estado.TERMINADO);

        Button volverButton = new Button("Volver", event -> {
            getUI().ifPresent(ui -> ui.navigate("home"));
        });

        add(tituloField, descripcionField, presupuestoField, guardarButton, volverButton);;
    }
    
}
