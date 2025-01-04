// package es.uca.iw.webituca.Views.Home;

// import com.vaadin.flow.component.Composite;
// import com.vaadin.flow.component.UI;
// import com.vaadin.flow.component.button.Button;
// import com.vaadin.flow.component.grid.Grid;
// import com.vaadin.flow.component.html.Span;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.server.VaadinSession;
// import com.vaadin.flow.server.auth.AnonymousAllowed;

// import es.uca.iw.webituca.Layout.Footer;
// import es.uca.iw.webituca.Model.Proyecto;
// import es.uca.iw.webituca.Service.ProyectoService;

// import org.springframework.beans.factory.annotation.Autowired;
// import java.util.List;

// @Route(value = "home")
// @AnonymousAllowed
// public class HomeView extends Composite<VerticalLayout> {

//     @Autowired
//     private ProyectoService proyectoService;

//     public HomeView() {
//         VerticalLayout layout = getContent();
//         layout.setSizeFull(); // Asegura que el layout ocupe todo el espacio disponible
//         layout.setSpacing(true);
//         layout.setPadding(true);

//         layout.add(new Span("Esto es Home view, aquí se mostrará información de proyectos actuales"));

//         // Verificación de inicio de sesión
//         String userLogado = (String) VaadinSession.getCurrent().getAttribute("user");

//         // Mostrar lista de proyectos
//         List<Proyecto> proyectos = proyectoService.listarProyectos(); // Se asume que retorna una lista directamente

//         // Configuración de la Grid
//         Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
//         grid.addColumn(Proyecto::getTitulo).setHeader("Titulo");
//         grid.addColumn(Proyecto::getDescripcion).setHeader("Descripcion");
//         grid.addColumn(proyecto -> proyecto.isActivo() ? "Activo" : "Inactivo").setHeader("Estado");

//         // Columna dinámica para gestión de proyectos según permisos del usuario
//         grid.addComponentColumn(proyecto -> {
//             if (proyecto.isPermisoGestion()) {
//                 return new Button("Gestionar", event -> gestionarProyecto(proyecto));
//             } else {
//                 return new Span(); // No mostrar botón si no tiene permisos
//             }
//         }).setHeader("Acción");

//         layout.add(grid);

//         // Mostrar botones de sesión
//         if (userLogado == null) {
//             // Redirigir al login si no hay usuario en sesión
//             Button loginButton = new Button("Iniciar sesión", event -> UI.getCurrent().navigate("login"));
//             layout.add(loginButton);
//         } else {
//             layout.add(new Span("Bienvenido, " + userLogado + "!"));

//             // Botón de logout
//             Button logoutButton = new Button("Cerrar sesión", event -> {
//                 VaadinSession.getCurrent().setAttribute("user", null); // Limpiar la sesión
//                 UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del usuario
//             });
//             layout.add(logoutButton);
//         }

//         // Añadir el footer
//         layout.add(new Footer());
//     }

//     private void gestionarProyecto(Proyecto proyecto) {
//         UI.getCurrent().navigate("proyecto/" + proyecto.getTitulo());
// }
// }



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
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Views.AgregarProyectoView;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "home")
@AnonymousAllowed
public class HomeView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;

    @Autowired
    public HomeView(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;

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
        grid.addColumn(proyecto -> proyecto.isActivo() ? "Activo" : "Inactivo").setHeader("Estado");

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

        // Mostrar botones de sesión
        if (userLogado == null) {
            // Redirigir al login si no hay usuario en sesión
            Button loginButton = new Button("Iniciar sesión", event -> UI.getCurrent().navigate("login"));
            layout.add(loginButton);
        } else {
            layout.add(new Span("Bienvenido, " + userLogado + "!"));

            // Botón de logout
            Button logoutButton = new Button("Cerrar sesión", event -> {
                VaadinSession.getCurrent().setAttribute("user", null); // Limpiar la sesión
                UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del usuario
            });
            layout.add(logoutButton);

            Button agregar = new Button("Agregar Proyecto");
            agregar.addClickListener(e -> UI.getCurrent().navigate(AgregarProyectoView.class));
            agregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(agregar);
        }


    }

    private void gestionarProyecto(Proyecto proyecto) {
        UI.getCurrent().navigate("proyecto/" + proyecto.getTitulo());
}
}