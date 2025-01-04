package es.uca.iw.webituca.Views.Home;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Views.VistasAdmin.CarteraMenuView;
import es.uca.iw.webituca.Views.VistasAdmin.UsuarioUpdateView;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "/home-admin")
//@AnonymousAllowed
@RolesAllowed("Admin")
@PageTitle("Home-Admin")
public class HomeAdminView extends Composite<VerticalLayout> {
    private final AuthenticatedUser authenticatedUser;
    public HomeAdminView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        crearTitulo();

        botones();
         Button logoutButton = new Button("Cerrar sesión", event -> {
                authenticatedUser.logout(); // Limpiar la sesión
                UI.getCurrent().getPage().reload(); // Recargar la página para actualizar el estado del usuario
            });
            if(authenticatedUser.get().isPresent()) {
                getContent().add(logoutButton);
            }
            else {
                logoutButton.setVisible(false);
            }
            
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

        Button carteras = new Button("Carteras");
        carteras.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        carteras.addClickListener(e -> {
            carteras.getUI().ifPresent(ui -> ui.navigate(CarteraMenuView.class));
        });

        horizontalLayout.add(proyectos, usuarios, carteras);
        getContent().add(horizontalLayout);

    }
}
