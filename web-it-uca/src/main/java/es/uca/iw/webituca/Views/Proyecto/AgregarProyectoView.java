package es.uca.iw.webituca.Views.Proyecto;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Model.Estado;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Service.UsuarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;




@AnonymousAllowed
@Route("agregar-proyecto")
public class AgregarProyectoView extends VerticalLayout {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioService usuarioService;

    public AgregarProyectoView() {
        FormLayout formLayout = new FormLayout();

        TextField tituloField = new TextField("Título");
        TextArea descripcionField = new TextArea("Descripción");
        DatePicker fechaInicioField = new DatePicker("Fecha de Inicio");
        DatePicker fechaFinField = new DatePicker("Fecha de Fin");
        //NumberField puntuacion1Field = new NumberField("Puntuación OTP");
        //NumberField puntuacion2Field = new NumberField("Puntuación Avalador");
        //NumberField prioridadField = new NumberField("Prioridad");
        NumberField presupuestoField = new NumberField("Presupuesto");
        TextField carteraField = new TextField("Cartera");
        TextField usuarioField = new TextField("Usuario");
        ComboBox<Usuario> avalador = new ComboBox<>();
        avalador.setId("Avalador");
        avalador.setLabel("Avalador");
        avalador.setPlaceholder("Seleccione Avalador");
        avalador.getElement().setAttribute("aria-label", "Seleccione al avalador");

        

        Button saveButton = new Button("Guardar", event -> {
            Proyecto proyecto = new Proyecto();
            proyecto.setTitulo(tituloField.getValue());
            proyecto.setDescripcion(descripcionField.getValue());
            proyecto.setEstado(Estado.EN_TRAMITE);
            proyecto.setFechaInicio(fechaInicioField.getValue().atStartOfDay());
            proyecto.setFechaFin(fechaFinField.getValue().atStartOfDay());
            //proyecto.setPuntuacion1(puntuacion1Field.getValue().floatValue());
            //proyecto.setPuntuacion2(puntuacion2Field.getValue().floatValue());
            //proyecto.setPrioridad(prioridadField.getValue().intValue());
            
            


               
            List<Usuario> avaladores = usuarioService.getAvaladores();
            avalador.setItems(avaladores);
            avalador.setItemLabelGenerator(Usuario::getNombre);
            // Aquí deberías buscar y asignar las entidades Cartera, Usuario y Avalador
            // proyecto.setCartera(buscarCarteraPorNombre(carteraField.getValue()));
            // proyecto.setUsuario(buscarUsuarioPorNombre(usuarioField.getValue()));
            // proyecto.setAvalador(buscarUsuarioPorNombre(avaladorField.getValue()));

            proyectoService.save(proyecto);
            Notification.show("Proyecto guardado");
        });

        formLayout.add(tituloField, descripcionField, fechaInicioField, fechaFinField, presupuestoField, carteraField, usuarioField, avalador);
        add(formLayout, saveButton);
    }


 

}


        