package es.uca.iw.webituca.Views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.UI;

@Route(value = "activar")
@AnonymousAllowed
public class ActivacionUsuarioView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private String mensaje;

    // Método para recibir parámetros antes de la entrada en la vista
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters params = event.getLocation().getQueryParameters();
        this.mensaje = params.getParameters().getOrDefault("mensaje", java.util.Collections.singletonList("¡Error en la activación!")).get(0);
    }

    public ActivacionUsuarioView() {

        Span mensajeExito = new Span(mensaje);

        mensajeExito.getStyle()
                .set("font-size", "20px")
                .set("color", mensaje.contains("exitosamente") ? "#28a745" : "#dc3545")
                .set("font-weight", "bold");

        Button botonLogin = new Button("Login", event -> UI.getCurrent().navigate("login"));
        botonLogin.getStyle()
                .set("margin-top", "20px")
                .set("background-color", "#007bff")
                .set("color", "#fff");

        // Layout centralizado
        VerticalLayout layout = getContent();
        layout.setDefaultHorizontalComponentAlignment(VerticalLayout.Alignment.CENTER);
        layout.setHeight("100vh");
        layout.add(mensajeExito, botonLogin);
    }
}



