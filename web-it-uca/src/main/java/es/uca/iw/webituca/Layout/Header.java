package es.uca.iw.webituca.Layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uca.iw.webituca.Views.LoginView;
import es.uca.iw.webituca.Views.Home.HomeView;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;


public class Header extends Composite<VerticalLayout> {
    
    public Header() {
        VerticalLayout container = getContent();
        container.setWidthFull();
        container.getStyle().set("background-color", "#2c3e50");
        container.getStyle().set("padding", "10px 20px");
        container.setSpacing(false);

        // Título y logo
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Image logo = new Image("Layouts/Logo_UCA.png", "UCA Logo");
        logo.setHeight("50px");
        logo.setWidth("auto"); // Esto garantiza que la imagen mantenga la proporción original.

        H1 title = new H1("Universidad de Cádiz");
        title.getStyle().set("color", "white");
        title.getStyle().set("margin", "0");

        topBar.add(logo, title);
        topBar.setAlignItems(Alignment.CENTER);
        topBar.expand(title);  // Expande el título para separar el logo

        // Barra de navegación
        Nav nav = new Nav();
        nav.getStyle().set("display", "flex");
        nav.getStyle().set("justify-content", "space-between");
        nav.getStyle().set("background-color", "#34495e");
        nav.getStyle().set("padding", "10px 20px");

        // Enlaces de navegación
        RouterLink homeLink = new RouterLink();
        homeLink.add(new Icon(VaadinIcon.HOME));
        homeLink.getStyle().set("color", "white");
        homeLink.setRoute(HomeView.class);

        RouterLink loginLink = new RouterLink();
        loginLink.add(new Icon(VaadinIcon.USER));
        loginLink.getStyle().set("color", "white");
        loginLink.setRoute(LoginView.class);

        // Añadir los enlaces a la barra de navegación
        nav.add(homeLink, loginLink);
        container.add(topBar, nav);  // Añadir el contenido a la pantalla


        
    }
}