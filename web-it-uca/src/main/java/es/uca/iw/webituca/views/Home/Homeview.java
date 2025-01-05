package es.uca.iw.webituca.Views.Home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Layout.Footer;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Views.Proyecto.AgregarProyectoView;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Views.MainLayout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "home")
@AnonymousAllowed
//@RolesAllowed("Admin")
public class HomeView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;


    @Autowired
    
    public HomeView(ProyectoService proyectoService, AuthenticatedUser authenticatedUser) {

        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        

        VerticalLayout layout = getContent();
        layout.setSizeFull(); // Asegura que el layout ocupe todo el espacio disponible
        layout.setSpacing(true);
        layout.setPadding(true);

        layout.add(new Span("Esto es Home view, aquí se mostrará información de proyectos actuales"));

        // Verificación de inicio de sesión
        String userLogado = (String) VaadinSession.getCurrent().getAttribute("user");

        // Obtener lista de proyectos desde el servicio
        List<Proyecto> proyectos = proyectoService.listarProyectos(); // Obtiene lista de proyectos

        // Configuración de la Grid con la lista de proyectos
        Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
        grid.setItems(proyectos); // Establece los elementos en el Grid
        grid.addColumn(Proyecto::getTitulo).setHeader("Titulo");
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripcion");


        // Columna dinámica para gestión de proyectos según permisos del usuario
        grid.addComponentColumn(proyecto -> {
            if (proyecto.isPermisoGestion()) {
                return new Button("Gestionar", event -> gestionarProyecto(proyecto));
            } else {
                return new Span(); // No mostrar botón si no tiene permisos
            }
        }).setHeader("Acción");


        List<Proyecto> proyectos_lista = proyectoService.listarProyectos();
        grid.setItems(proyectos_lista);
        layout.add(grid);
        
        

       //muestra nombre de usuario registrado actualmente
            if(authenticatedUser.get().isPresent()) {
                Span mensaje_bienvenido = new Span();
                mensaje_bienvenido.setText("Bienvenido, usuario " + authenticatedUser.get().get().getUsername());
                mensaje_bienvenido.getElement().setAttribute("aria-label", "Bienvenido, usuario");
                mensaje_bienvenido.getStyle().set("color", "blue");
                getContent().add(mensaje_bienvenido);
            }
            else {
                Span mensaje_bienvenido = new Span();
                mensaje_bienvenido.setText("Bienvenido, usuario invitado");
                mensaje_bienvenido.getElement().setAttribute("aria-label", "Bienvenido, usuario");
                mensaje_bienvenido.getStyle().set("color", "blue");
                getContent().add(mensaje_bienvenido);
            }


            Button agregar = new Button("Agregar Proyecto");
            agregar.addClickListener(e -> UI.getCurrent().navigate(AgregarProyectoView.class));
            agregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(agregar);
        

            Button logoutButton = new Button("Cerrar sesión", event -> {
                authenticatedUser.logout(); // Limpiar la sesión
                UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del usuario
            });
            if(authenticatedUser.get().isPresent()) {
                layout.add(logoutButton);
            }
            else {
                logoutButton.setVisible(false);
            }
            

    }

    private void gestionarProyecto(Proyecto proyecto) {
        UI.getCurrent().navigate("proyecto/" + proyecto.getTitulo());
}
}