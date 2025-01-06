package es.uca.iw.webituca.Views;

import com.vaadin.flow.component.Composite;
/*import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;*/
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.webituca.Layout.Header;
import es.uca.iw.webituca.Layout.Footer;

@Layout
@AnonymousAllowed
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {


    public MainLayout() {
        Header header = new Header();
        Footer footer = new Footer();
        footer.getElement().getStyle().set("order", "999");

        getContent().add(header, footer);
    }
  

}
