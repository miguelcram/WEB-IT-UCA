package es.uca.iw.webituca.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.webituca.Layout.Header;
import es.uca.iw.webituca.Layout.Footer;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private Header header;
    private Footer footer;

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        //setSizeFull(); // Asegura que el MainLayout ocupe todo el espacio disponible
        addHeaderContent();
        addFooterContent();

    }

    private void addHeaderContent() {
        header = new Header();
        addToNavbar(header);
    }

    private void addFooterContent() {
        footer = new Footer();
        // Añadir el footer al contenido principal
        addToDrawer(footer);
       
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
       // header.setTitle(getCurrentPageTitle());
    }
/* 
    private String getCurrentPageTitle() {
        return "Current Page Title"; // Puedes ajustar esto según tus necesidades
    }

    */
}
