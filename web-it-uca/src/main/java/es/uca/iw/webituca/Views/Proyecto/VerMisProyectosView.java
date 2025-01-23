package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

import java.time.format.DateTimeFormatter;
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
        grid.addColumn(Proyecto::getTitulo).setHeader("Título").setAutoWidth(true);
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción").setAutoWidth(true);
        DateTimeFormatter formatoTabla = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        grid.addColumn(proyecto -> proyecto.getFechaInicio().format(formatoTabla)).setHeader("Fecha Inicio").setAutoWidth(true);
        grid.addColumn(proyecto -> proyecto.getFechaFin().format(formatoTabla)).setHeader("Fecha Fin").setAutoWidth(true);
        grid.addColumn(Proyecto::getInteresados).setHeader("Interesados").setAutoWidth(true);
        grid.addColumn(Proyecto::getAlcance).setHeader("Alcance").setAutoWidth(true);
        grid.addColumn(Proyecto::getEstado).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(Proyecto::getPrioridad).setHeader("Prioridad").setAutoWidth(true);
        grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto").setAutoWidth(true);

        // Columna de botones para editar/ver proyectos
        grid.addComponentColumn(proyecto -> {
            Button editarVerButton = new Button("Editar", click -> {
                editarProyecto(proyecto);
            });
            editarVerButton.setEnabled(proyecto.getEstado() != Estado.RECHAZADO && proyecto.getEstado() != Estado.TERMINADO);
            editarVerButton.setWidth("80px");
            return editarVerButton;
        }).setHeader("Acción").setAutoWidth(true);

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

        TextArea interesadosField = new TextArea("Interesados");
        interesadosField.setValue(proyecto.getInteresados());
        interesadosField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        TextArea alcanceField = new TextArea("Alcance");
        alcanceField.setValue(proyecto.getAlcance());
        alcanceField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        TextField presupuestoField = new TextField("Presupuesto");
        presupuestoField.setValue(String.valueOf(proyecto.getPresupuesto()));
        presupuestoField.setReadOnly(proyecto.getEstado() == Estado.RECHAZADO || proyecto.getEstado() == Estado.TERMINADO);

        HorizontalLayout row1 = new HorizontalLayout(tituloField, descripcionField);
        HorizontalLayout row2 = new HorizontalLayout(interesadosField, alcanceField);
        HorizontalLayout row3 = new HorizontalLayout(presupuestoField);

        Button guardarButton = new Button("Guardar cambios", event -> {
            proyecto.setTitulo(tituloField.getValue());
            proyecto.setDescripcion(descripcionField.getValue());
            proyecto.setInteresados(interesadosField.getValue());
            proyecto.setAlcance(alcanceField.getValue());
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

        add(row1, row2, row3, guardarButton, volverButton);
    }
    
}
