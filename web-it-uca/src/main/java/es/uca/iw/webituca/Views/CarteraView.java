package es.uca.iw.webituca.Views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Service.CarteraService;

@Route(value = "cartera")
@PageTitle("Cartera-Actual")
@AnonymousAllowed
public class CarteraView extends Composite<VerticalLayout> {
    
    @Autowired
    private CarteraService carteraService;

    public CarteraView() {

        // Obtén la cartera activa a través del servicio
        Cartera cartera = carteraService.getCarteraActual().orElse(null);

        if (cartera == null) {
            H2 errorMessage = new H2("No hay una cartera activa en este momento.");
            errorMessage.getStyle().set("color", "black");
            getContent().add(errorMessage);
        } else {
            H1 titulo = new H1("Cartera Actual");
            getContent().add(titulo);

            FormLayout mostrar = new FormLayout();

            TextField nombreField = new TextField("Nombre");
            nombreField.setValue(cartera.getNombre());
            nombreField.setReadOnly(true);

            TextField descripcionField = new TextField("Descripcion");
            descripcionField.setValue(cartera.getDescripcion());
            descripcionField.setReadOnly(true);

            DatePicker fechacreacionField = new DatePicker("Fecha de Inicio");
            fechacreacionField.setValue(cartera.getFechaCreacion().toLocalDate());
            fechacreacionField.setReadOnly(true);

            DatePicker fechafinField = new DatePicker("Fecha de Fin");
            fechafinField.setValue(cartera.getFechaFin().toLocalDate());
            fechafinField.setReadOnly(true);

            NumberField numerohorasField = new NumberField("Numero de Horas");
            numerohorasField.setValue((double) cartera.getNumero_horas());
            numerohorasField.setReadOnly(true);

            NumberField presupuestoField = new NumberField("Presupuesto");
            presupuestoField.setValue((double) cartera.getPresupuesto());
            presupuestoField.setReadOnly(true);

            mostrar.add(nombreField, descripcionField, fechacreacionField, fechafinField, numerohorasField, presupuestoField);
            getContent().add(mostrar);
        }
    }
}
