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
import es.uca.iw.webituca.Config.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
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

        H1 tituloCartera = new H1("Información de la Cartera Actual");
        Span nombreCartera = new Span("Nombre: " + carteraActual.getNombre());
        Span descripcionCartera = new Span("Descripción: " + carteraActual.getDescripcion());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Span fechaCreacionCartera = new Span("Fecha de Creación: " + carteraActual.getFechaCreacion().format(formato));
        Span fechaFinCartera = new Span("Fecha de Cierre: " + carteraActual.getFechaFin().format(formato));
        Span numHoras = new Span("Número de horas: " + carteraActual.getNumero_horas());
        Span presupuestoCartera = new Span("Presupuesto: " + carteraActual.getPresupuesto() + " €");

        layout.add(tituloCartera, nombreCartera, descripcionCartera, fechaCreacionCartera, fechaFinCartera, numHoras, presupuestoCartera);

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
            grid.addColumn(Proyecto::getTitulo).setHeader("Título").setAutoWidth(true);
            grid.addColumn(Proyecto::getDescripcion).setHeader("Descripción").setAutoWidth(true);
            DateTimeFormatter formatoTabla = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            grid.addColumn(proyecto -> proyecto.getFechaInicio().format(formatoTabla)).setHeader("Fecha Inicio").setAutoWidth(true);
            grid.addColumn(proyecto -> proyecto.getFechaFin().format(formatoTabla)).setHeader("Fecha Fin").setAutoWidth(true);
            grid.addColumn(Proyecto::getInteresados).setHeader("Interesados").setAutoWidth(true);
            grid.addColumn(Proyecto::getAlcance).setHeader("Alcance").setAutoWidth(true);
            grid.addColumn(Proyecto::getEstado).setHeader("Estado").setAutoWidth(true);
            grid.addColumn(Proyecto::getPresupuesto).setHeader("Presupuesto").setAutoWidth(true);
            grid.setItems(proyectosFiltrados);

            layout.add(grid);
        }

    }
}
