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

import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Config.AuthenticatedUser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "home")
@AnonymousAllowed
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

        String userLogado = (String) VaadinSession.getCurrent().getAttribute("user");

        // Obtener lista de proyectos desde el servicio
        List<Proyecto> proyectos = proyectoService.listarProyectos();

        // Configuración de la Grid con la lista de proyectos
        Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
        grid.setItems(proyectos); // Establece los elementos en el Grid
        grid.addColumn(Proyecto::getTitulo).setHeader("Titulo");
        grid.addColumn(Proyecto::getDescripcion).setHeader("Descripcion");

        //List<Proyecto> proyectos_lista = proyectoService.listarProyectos();
        grid.setItems(proyectos);
        layout.add(grid);
        
        //Muestra nombre de usuario registrado actualmente
        if(authenticatedUser.get().isPresent()) {
            Span mensaje_bienvenido = new Span();
            mensaje_bienvenido.setText("Bienvenido, usuario " + authenticatedUser.get().get().getUsername());
            mensaje_bienvenido.getElement().setAttribute("aria-label", "Bienvenido, usuario");
            mensaje_bienvenido.getStyle().set("color", "blue");
            getContent().add(mensaje_bienvenido);
        } else {
            Span mensaje_bienvenido = new Span();
            mensaje_bienvenido.setText("Bienvenido, usuario invitado");
            mensaje_bienvenido.getElement().setAttribute("aria-label", "Bienvenido, usuario");
            mensaje_bienvenido.getStyle().set("color", "blue");
            getContent().add(mensaje_bienvenido);
        }

        // Botón para agregar proyectos
        Button agregar = new Button("Agregar Proyecto");
        agregar.addClickListener(e -> UI.getCurrent().navigate("proyecto/nuevo"));
        agregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(agregar);

        // Botón para avalar proyectos para AVALADORES
        if (authenticatedUser.get().isPresent() && 
            authenticatedUser.get().get().getRol() == Rol.AVALADOR) {
            Button avalarProyectosButton = new Button("Avalar Proyectos");
            avalarProyectosButton.addClickListener(e -> UI.getCurrent().navigate("proyecto/avalar"));
            //avalarProyectosButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(avalarProyectosButton);
        }
        
        Button logoutButton = new Button("Cerrar sesión", event -> {
            authenticatedUser.logout(); // Limpiar la sesión
            UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del usuario
        });
        
        if(authenticatedUser.get().isPresent()) {
            layout.add(logoutButton);
        } else {
            logoutButton.setVisible(false);
        }
    }

    /*private void gestionarProyecto(Proyecto proyecto) {
        UI.getCurrent().navigate("proyecto/" + proyecto.getId());
    }*/
}