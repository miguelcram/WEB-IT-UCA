package es.uca.iw.webituca.Layout;

import com.vaadin.flow.component.Composite;
// import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Footer extends Composite<HorizontalLayout> {
    public Footer() {
        HorizontalLayout layout = getContent();
        layout.setWidthFull();
        layout.add(new Span("© 2023 Your Company"));
        layout.add(new Span(" | "));
        layout.add(new Span("Privacy Policy"));
        layout.add(new Span(" | "));
        layout.add(new Span("Terms of Service"));
    }
}