package es.uca.iw.webituca.Views.Home;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.CarteraService;
import es.uca.iw.webituca.Service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "Inicio")
@PageTitle("Proyectos Aprobados y Finalizados")
@AnonymousAllowed
public class InicioView extends Composite<VerticalLayout> {

    private final CarteraService carteraService;
    private final ProyectoService proyectoService;

    @Autowired
    public InicioView(CarteraService carteraService, ProyectoService proyectoService) {
        this.carteraService = carteraService;
        this.proyectoService = proyectoService;

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
            grid.addColumn(Proyecto::getEstado).setHeader("Etiqueta");
            grid.setItems(proyectosFiltrados);

            layout.add(grid);
        }
    }
}
