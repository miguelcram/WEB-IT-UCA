package es.uca.iw.webituca.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import es.uca.iw.webituca.Layout.Header;
import es.uca.iw.webituca.Layout.Footer;

import java.util.List;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        //setPrimarySection(Section.DRAWER);

        addHeaderContent();
        addFooterContent();
    }

    private void addHeaderContent() {
        viewTitle = new H1();
        Header header = new Header();
      //  HorizontalLayout headerLayout = new HorizontalLayout(viewTitle, header);
      //  headerLayout.setWidthFull();
      //  headerLayout.expand(viewTitle);
        addToNavbar(header);
    }

    private void addFooterContent() {
        Footer footer = new Footer();
        addToDrawer(footer);
    }



    
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}
