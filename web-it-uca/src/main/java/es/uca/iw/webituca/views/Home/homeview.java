package es.uca.iw.webituca.views.Home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/home")

public class homeview extends Composite<VerticalLayout> {
    public homeview() {
        getContent().add(new Span("Hello, World!"));
    }
    
}
