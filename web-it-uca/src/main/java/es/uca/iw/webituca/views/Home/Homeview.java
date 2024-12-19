package es.uca.iw.webituca.Views.Home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Layout.Footer;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.ProyectoService;

import java.util.List;

@Route(value = "home")
@AnonymousAllowed
public class Homeview extends Composite<VerticalLayout> {
    public Homeview() {
        VerticalLayout layout = getContent();
        layout.setSizeFull(); // Asegura que el layout ocupe todo el espacio disponible

        layout.add(new Span("Esto es Home view, aquí se mostrará información de proyectos actuales"));
        
        //Verificación de inicio de sesion
        String userLogado = (String) VaadinSession.getCurrent().getAttribute("user");

        // Mostrar lista de proyectos
        ProyectoService proyectoService = new ProyectoService();
        List<Proyecto> proyectos = proyectoService.listarProyectos(userLogado);

        Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
        grid.addColumn(Proyecto::getNombre).setHeader("Nombre");
        grid.addColumn(proyecto -> proyecto.isActivo() ? "Activo" : "Inactivo").setHeader("Estado");

        // Añadir columna dinámica para gestión de proyectos
        if (userLogado != null) {
            grid.addComponentColumn(proyecto -> {
                if (proyecto.isPermisoGestion()) {
                    return new Button("Gestionar", event -> gestionarProyecto(proyecto));
                } else {
                    return new Span(); // No mostrar botón si no tiene permisos
                }
            }).setHeader("Acción");
        }

        layout.add(grid);

        if (userLogado == null) {
            // Redirigir al login si no hay usuario en sesión
            Button loginButton = new Button("Iniciar sesión", event -> UI.getCurrent().navigate("login"));
            layout.add(loginButton);
        } else {
            layout.add(new Span("Bienvenido, " + userLogado + "!"));
            //Boton de logout
            Button logoutButton = new Button("Cerrar sesión", event -> {
                VaadinSession.getCurrent().setAttribute("user", null); // Limpiar la sesión
                UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del user
            });
            layout.add(logoutButton);
        }

        //layout.add(new Footer());
    }

    private void gestionarProyecto(Proyecto proyecto) {
        UI.getCurrent().navigate("proyecto/" + proyecto.getNombre());
    }
}