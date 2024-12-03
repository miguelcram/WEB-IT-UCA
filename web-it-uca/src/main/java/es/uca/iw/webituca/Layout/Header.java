package es.uca.iw.webituca.Layout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;

public class Header extends Composite<HorizontalLayout> {
    private H1 title;

    public Header() {
        HorizontalLayout layout = getContent();
        layout.setWidthFull();
        layout.getStyle().set("background-color", "lightgreen");
        layout.setPadding(true);
        layout.setSpacing(true);

        title = new H1("PROYECTOS IW PRUEBAS");
        title.getStyle().set("margin", "0");

        Button homeButton = new Button("Home", event -> UI.getCurrent().navigate("/home"));
        Button aboutButton = new Button("About", event -> UI.getCurrent().navigate("/about"));
        Button contactButton = new Button("Contact", event -> UI.getCurrent().navigate("/contact"));
        Button loginButton = new Button("Login", event -> UI.getCurrent().navigate("/login"));

        layout.add(title, homeButton, aboutButton, contactButton, loginButton);
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }
}
