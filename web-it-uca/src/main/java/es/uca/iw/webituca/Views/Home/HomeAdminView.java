package es.uca.iw.webituca.Views.Home;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Views.VistasAdmin.UsuarioUpdateView;

@Route(value = "/home-admin")
@AnonymousAllowed
@PageTitle("Home-Admin")
public class HomeAdminView extends Composite<VerticalLayout> {
    public HomeAdminView() {

        crearTitulo();

        botones();
    }

    private void crearTitulo() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        H1 titulo = new H1("Home-Admin");
        horizontalLayout.add(titulo);

        getContent().add(horizontalLayout);
    }

    private void botones() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        Button proyectos = new Button("Proyectos");
        proyectos.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button usuarios = new Button("Usuarios");
        usuarios.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        usuarios.addClickListener(e -> {
            usuarios.getUI().ifPresent(ui -> ui.navigate(UsuarioUpdateView.class));
        });

        horizontalLayout.add(proyectos, usuarios);
        getContent().add(horizontalLayout);

    }
}
