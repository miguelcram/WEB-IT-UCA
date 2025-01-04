package es.uca.iw.webituca.Layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Header extends Composite<VerticalLayout> {

    public Header() {
        VerticalLayout container = getContent();
        container.setWidthFull();
        container.getStyle().set("background-color", "#2c3e50");
        container.getStyle().set("padding", "10px 20px");

        // Título y logo
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);

        Image logo = new Image("frontend/images/logo.png", "UCA Logo");
        logo.setHeight("50px");

        H1 title = new H1("Universidad de Cádiz");
        title.getStyle().set("color", "white");
        title.getStyle().set("margin", "0");

        topBar.add(logo, title);
        topBar.setAlignItems(Alignment.CENTER);
        topBar.expand(title);

        // Barra de navegación
        Nav nav = new Nav();
        nav.getStyle().set("display", "flex");
        nav.getStyle().set("justify-content", "center");
        nav.getStyle().set("background-color", "#34495e");
        nav.getStyle().set("padding", "10px 0");

        RouterLink estudiantes = new RouterLink();
        RouterLink investigacion = new RouterLink();
        RouterLink internacional = new RouterLink();
        RouterLink vidaUniversitaria = new RouterLink();
        //RouterLink uca = new RouterLink("UCA", YourViewClass.class);

        estudiantes.getStyle().set("color", "white");
        investigacion.getStyle().set("color", "white");
        internacional.getStyle().set("color", "white");
        vidaUniversitaria.getStyle().set("color", "white");
        RouterLink uca = new RouterLink();
        uca.getStyle().set("color", "white");

        nav.add(estudiantes, investigacion, internacional, vidaUniversitaria, uca);

        container.add(topBar, nav);
    }
}
