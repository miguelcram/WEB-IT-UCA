package es.uca.iw.webituca.Views.Home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Service.CarteraService;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Config.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "Inicio")
@PageTitle("Proyectos Aprobados y Finalizados")
@AnonymousAllowed
public class InicioView extends Composite<VerticalLayout> {

    private CarteraService carteraService;
    private ProyectoService proyectoService;
    private AuthenticatedUser authenticatedUser;

    @Autowired
    public InicioView(CarteraService carteraService, ProyectoService proyectoService, AuthenticatedUser authenticatedUser) {
        this.carteraService = carteraService;
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;

        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);

        // Obtener la cartera activa
        Cartera carteraActual = carteraService.getCarteraActual().orElse(null);

        if (carteraActual == null) {
            layout.add(new H1("No hay una cartera activa en este momento."));
            return;
        }

        // Mensaje de bienvenida
        Span mensajeBienvenida = new Span("Bienvenido a la sección de proyectos de la cartera actual.");
        mensajeBienvenida.getStyle().set("font-size", "18px");
        mensajeBienvenida.getStyle().set("color", "blue");

        layout.add(new H1("Proyectos Aprobados o Finalizados"), mensajeBienvenida);

        // Filtrar proyectos con etiqueta "aprobados" o "finalizados"
        List<Proyecto> proyectosFiltrados = proyectoService.listarProyectos().stream()
                .filter(proyecto -> proyecto.getCartera().getId().equals(carteraActual.getId()))
                .filter(proyecto -> proyecto.getEstado().toString().equalsIgnoreCase("ACEPTADO") ||
                                    proyecto.getEstado().toString().equalsIgnoreCase("TERMINADO"))
                .collect(Collectors.toList());

        if (proyectosFiltrados.isEmpty()) {
            layout.add(new Span("No hay proyectos con las etiquetas 'aprobados' o 'finalizados' en la cartera actual."));
        } else {
            // Configuración de la grid
            Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);
            grid.addColumn(Proyecto::getTitulo).setHeader("Título");
            grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción");
            grid.addColumn(Proyecto::getEstado).setHeader("Estado");
            grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto");
            grid.setItems(proyectosFiltrados);

            layout.add(grid);
        }

        // Botón para agregar proyectos
        if (authenticatedUser.get().isPresent()) {
            Button agregar = new Button("Agregar Proyecto");
            agregar.addClickListener(e -> UI.getCurrent().navigate("proyecto/nuevo"));
            agregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(agregar);
        }

        // Botón para ver proyectos
        if (authenticatedUser.get().isPresent()) {
            Button verProyectos = new Button("Ver Mis Proyectos");
            verProyectos.addClickListener(e -> UI.getCurrent().navigate("proyecto/ver-proyectos"));
            layout.add(verProyectos);
        }

        // Botón para avalar proyectos para AVALADORES
        if (authenticatedUser.get().isPresent() && 
            authenticatedUser.get().get().getRol() == Rol.AVALADOR) {
            Button avalarProyectosButton = new Button("Avalar Proyectos");
            avalarProyectosButton.addClickListener(e -> UI.getCurrent().navigate("proyecto/avalar"));
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
}
