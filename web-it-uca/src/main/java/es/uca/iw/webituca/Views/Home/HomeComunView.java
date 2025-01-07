package es.uca.iw.webituca.Views.Home;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Config.AuthenticatedUser;
import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@PageTitle("Home-Home")
@Route("home-home")
@AnonymousAllowed
public class HomeComunView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public HomeComunView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        VerticalLayout layout = getContent();
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setPadding(true);

        // Título
        H1 titulo = new H1("Bienvenido a la Plataforma");
        layout.add(titulo);

        // Obtener el usuario autenticado
        Optional<Usuario> usuarioOpt = authenticatedUser.get();

        // Mensaje de bienvenida
        Div mensajeBienvenida = new Div();
        usuarioOpt.ifPresentOrElse(
            usuario -> {
                mensajeBienvenida.setText("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
                agregarBotonesPorRol(layout, usuario.getRol()); // Generar botones automáticamente
            },
            () -> mensajeBienvenida.setText("Bienvenido, usuario invitado")
        );
        layout.add(mensajeBienvenida);
    }

    private void agregarBotonesPorRol(VerticalLayout layout, Rol rol) {
        if (rol == null) {
            layout.add(new Div(new Button("No hay acciones disponibles para este rol")));
            return;
        }

        switch (rol) {
            case Admin:
                layout.add(crearBoton("Administrar Proyectos", "admin-proyectos"));
                layout.add(crearBoton("Administrar Usuarios", "admin-usuarios"));
                break;

            case Usuario:
                layout.add(crearBoton("Ver Proyectos", "ver-proyectos"));
                layout.add(crearBoton("Editar Perfil", "editar-perfil"));
                break;

            case Ceo:
                layout.add(crearBoton("Puntuar Proyectos", "puntuar-proyectos"));
                layout.add(crearBoton("Ver Reportes", "ver-reportes"));
                break;

            default:
                layout.add(new Div(new Button("No hay acciones disponibles para este rol")));
        }
    }

    private Button crearBoton(String texto, String ruta) {
        Button boton = new Button(texto);
        boton.addClickListener(e -> boton.getUI().ifPresent(ui -> ui.navigate(ruta)));
        return boton;
    }
}
