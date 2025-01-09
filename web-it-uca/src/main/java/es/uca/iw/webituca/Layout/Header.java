package es.uca.iw.webituca.Layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;

import es.uca.iw.webituca.Views.Home.HomeView;
import es.uca.iw.webituca.Views.Home.InicioView;
import es.uca.iw.webituca.Views.Usuario.LoginView;
import es.uca.iw.webituca.Config.AuthenticatedUser;

public class Header extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    public Header(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        VerticalLayout container = getContent();
        container.setWidthFull();
        container.getStyle().set("background-color", "#2c3e50");
        container.getStyle().set("padding", "10px 20px");
        container.setSpacing(false);

        // Título y logo
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Image logo = new Image("Layouts/Logo_UCA.png", "UCA Logo");
        logo.setHeight("50px");
        logo.setWidth("auto"); // Mantener proporción

        H1 title = new H1("Universidad de Cádiz");
        title.getStyle().set("color", "white");
        title.getStyle().set("margin", "0");

        topBar.add(logo, title);
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.expand(title); // Expande el título para separar el logo

        // Barra de navegación
        Nav nav = new Nav();
        nav.getStyle().set("display", "flex");
        nav.getStyle().set("align-items", "center");
        nav.getStyle().set("background-color", "#34495e");
        nav.getStyle().set("padding", "10px 20px");

        // Botones de navegación
        Button homeButton = new Button(new Icon(VaadinIcon.HOME));
        homeButton.getStyle().set("color", "white");
        homeButton.getStyle().set("background-color", "transparent");
        homeButton.getStyle().set("border", "none");
        homeButton.getStyle().set("cursor", "pointer");
        homeButton.addClickListener(event -> homeButton.getUI().ifPresent(ui -> ui.navigate(InicioView.class)));

        Button perfilButton = new Button(new Icon(VaadinIcon.USER));
        perfilButton.getStyle().set("color", "white");
        perfilButton.getStyle().set("background-color", "transparent");
        perfilButton.getStyle().set("border", "none");
        perfilButton.getStyle().set("cursor", "pointer");
        perfilButton.addClickListener(event -> perfilButton.getUI().ifPresent(ui -> ui.navigate(HomeView.class)));

        // Botón dinámico para login/logout
        Button authButton = new Button();
        authButton.getStyle().set("color", "white");
        authButton.getStyle().set("background-color", "transparent");
        authButton.getStyle().set("border", "none");
        authButton.getStyle().set("cursor", "pointer");

        if (authenticatedUser != null && authenticatedUser.get() != null) {
            authButton.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            authButton.addClickListener(event -> {
                authenticatedUser.logout();
                authButton.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            });
        } else {
            authButton.setIcon(new Icon(VaadinIcon.SIGN_IN));
            authButton.addClickListener(event -> authButton.getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
        }

        // Añadir los botones a la barra de navegación
        nav.add(homeButton, perfilButton, authButton);

        // Añadir los elementos al contenedor principal
        container.add(topBar, nav);
    }
}
