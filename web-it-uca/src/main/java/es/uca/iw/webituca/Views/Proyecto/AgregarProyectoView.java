package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.CarteraService;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import es.uca.iw.webituca.Config.AuthenticatedUser;

import java.io.InputStream;
import java.util.List;


@RolesAllowed({"Avalador","Ceo","Usuario", "Otp"})
@Route("agregar-proyecto")
public class AgregarProyectoView extends VerticalLayout {

    private final ProyectoService proyectoService;
    private final AuthenticatedUser authenticatedUser;
    private final CarteraService carteraService;
    private final UsuarioService usuarioService;

    public AgregarProyectoView(ProyectoService proyectoService, AuthenticatedUser authenticatedUser, CarteraService carteraService, UsuarioService usuarioService) {
        this.proyectoService = proyectoService;
        this.authenticatedUser = authenticatedUser;
        this.carteraService = carteraService;
        this.usuarioService = usuarioService;

        Cartera cartera = carteraService.getCarteraActual().orElse(null);
        if (cartera == null) {
            Notification.show("No hay Cartera disponible", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("home");
            return;
        }

        FormLayout formLayout = new FormLayout();

        TextField tituloField = new TextField("Título");
        TextArea descripcionField = new TextArea("Descripción");
        DatePicker fechaInicioField = new DatePicker("Fecha de Inicio");
        DatePicker fechaFinField = new DatePicker("Fecha de Fin");
        ComboBox<Usuario> avalador = new ComboBox<>();
        avalador.setId("Avalador");
        avalador.setLabel("Avalador");
        avalador.setPlaceholder("Seleccione Avalador");
        List<Usuario> avaladores = usuarioService.getAvaladores();
        avalador.setItems(avaladores);
        avalador.setItemLabelGenerator(Usuario::getNombre);



        //la subida
        // Campo de subida de archivos
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf");
        upload.setMaxFiles(1);
        upload.setMaxFileSize(10 * 1024 * 1024); // 10 MB
        upload.setDropLabel(new Span("Arrastra un archivo PDF aquí o haz clic para seleccionar"));

        


        Button saveButton = new Button("Guardar", event -> {
            Proyecto proyecto = new Proyecto();
            proyecto.setTitulo(tituloField.getValue());
            proyecto.setDescripcion(descripcionField.getValue());
            proyecto.setEstado(Estado.EN_TRAMITE);
            proyecto.setFechaInicio(fechaInicioField.getValue().atStartOfDay());
            proyecto.setFechaFin(fechaFinField.getValue().atStartOfDay());
            proyecto.setUsuario(authenticatedUser.get().get());
            proyecto.setCartera(cartera);
            proyecto.setAvalador(avalador.getValue());
            proyectoService.guardarProyecto(proyecto, buffer);
            Notification.show("Proyecto guardado");
            UI.getCurrent().access(() -> UI.getCurrent().navigate("/home"));
        });

        formLayout.add(tituloField, descripcionField, fechaInicioField, fechaFinField, avalador, upload);
        add(formLayout, saveButton);
    }
}