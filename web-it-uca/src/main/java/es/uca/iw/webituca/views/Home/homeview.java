package es.uca.iw.webituca.views.Home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;
import es.uca.iw.webituca.Layout.Footer;

@Route(value = "/home")
public class Homeview extends Composite<VerticalLayout> {
    public Homeview() {
        VerticalLayout layout = getContent();
        layout.add(new Span("Esto es Home view, aqui se mostrara informacion de proyectos actuales"));

        Button loginButton = new Button("Go to Login", event -> UI.getCurrent().navigate("/login"));
        layout.add(loginButton);

        layout.add(new Footer());
    }
}