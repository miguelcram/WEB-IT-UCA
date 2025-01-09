package es.uca.iw.webituca.Views.VistasAdmin;

import es.uca.iw.webituca.Views.Home.HomeView;
import jakarta.annotation.security.RolesAllowed;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.webituca.Model.Cartera;
import org.springframework.beans.factory.annotation.Autowired;
import es.uca.iw.webituca.Service.CarteraService;

import java.util.List;

@Route(value = "admin-cartera")
@PageTitle("Admin-Cartera")
@RolesAllowed("Admin")
//@AnonimousAllowed
public class CarteraMenuView extends Composite<VerticalLayout> {

    private final CarteraService carteraService;

    private final TextField nombreField = new TextField("Nombre");
    private final TextField descripcionField = new TextField("Descripcion");
    private final DatePicker fechacreacionField = new DatePicker("Fecha de Inicio");
    private final DatePicker fechafinField = new DatePicker("Fecha de Fin");
    private final NumberField numerohorasField = new NumberField("Numero de Horas");
    private final NumberField presupuestoField = new NumberField("Presupuesto");

    private final Grid<Cartera> carteraTabla = new Grid<>(Cartera.class);

    @Autowired
    public CarteraMenuView(CarteraService carteraService) {
        this.carteraService = carteraService;
        crearTitulo("Carteras");
        crearTabla();
        crearAdd();
        crearVolver();
        actualizarTabla();
    }

    private void crearTitulo(String titulo) {
        HorizontalLayout tituloLayout = new HorizontalLayout();
        H1 title = new H1(titulo);
        tituloLayout.add(title);
        tituloLayout.setWidthFull();
        getContent().add(tituloLayout);
    }

    private void crearTabla() {
        carteraTabla.setEmptyStateText("No hay Carteras");
        carteraTabla.removeAllColumns();

        carteraTabla.addColumn(Cartera::getNombre).setHeader("Nombre");
        carteraTabla.addComponentColumn(cartera -> {
            Button editButton = new Button(VaadinIcon.EDIT.create(), click -> editar(cartera));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return editButton;
        });
        carteraTabla.addComponentColumn(cartera -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> eliminar(cartera));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        });

        getContent().add(carteraTabla);
    }

    private void crearAdd() {
        HorizontalLayout addLayout = new HorizontalLayout();
        Button addButton = new Button("Añadir Cartera", event -> add());
        addLayout.add(addButton);
        addLayout.setWidthFull();
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().add(addLayout);
    }

    private void crearVolver() {
        HorizontalLayout volverLayout = new HorizontalLayout();
        Button volverButton = new Button("Volver");
        volverButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        volverButton.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));
        volverLayout.add(volverButton);
        volverLayout.setWidthFull();
        volverLayout.setAlignItems(FlexComponent.Alignment.END);
        volverLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        getContent().add(volverLayout);
    }

    private void editar(Cartera cartera) {
        Dialog editDialog = new Dialog();
        FormLayout form = new FormLayout();

        nombreField.setValue(cartera.getNombre());
        descripcionField.setValue(cartera.getDescripcion());
        fechacreacionField.setValue(cartera.getFechaCreacion().toLocalDate());
        fechafinField.setValue(cartera.getFechaFin().toLocalDate());
        numerohorasField.setValue((double) cartera.getNumero_horas());
        presupuestoField.setValue((double) cartera.getPresupuesto());

        form.add(nombreField, descripcionField, fechacreacionField, fechafinField, numerohorasField, presupuestoField);

        Button saveButton = new Button("Guardar", e -> {
            cartera.setNombre(nombreField.getValue());
            cartera.setDescripcion(descripcionField.getValue());
            cartera.setFechaCreacion(fechacreacionField.getValue().atStartOfDay());
            cartera.setFechaFin(fechafinField.getValue().atStartOfDay());
            cartera.setNumero_horas(numerohorasField.getValue().floatValue());
            cartera.setPresupuesto(presupuestoField.getValue().floatValue());
            carteraService.updateCartera(cartera.getId(), cartera);
            actualizarTabla();
            editDialog.close();
        });

        Button cancelButton = new Button("Cancelar", e -> editDialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        editDialog.add(form, buttons);
        editDialog.open();
    }

    private void add() {
        Dialog addDialog = new Dialog();
        addDialog.setHeaderTitle("Añadir Cartera");

        FormLayout form = new FormLayout();
        form.add(nombreField, descripcionField, fechacreacionField, fechafinField, numerohorasField, presupuestoField);

        Button saveButton = new Button("Guardar", e -> {
            Cartera cartera = new Cartera();
            cartera.setNombre(nombreField.getValue());
            cartera.setDescripcion(descripcionField.getValue());
            cartera.setFechaCreacion(fechacreacionField.getValue().atStartOfDay());
            cartera.setFechaFin(fechafinField.getValue().atStartOfDay());
            cartera.setNumero_horas(numerohorasField.getValue().floatValue());
            cartera.setPresupuesto(presupuestoField.getValue().floatValue());
            carteraService.createCartera(cartera);
            actualizarTabla();
            addDialog.close();
        });

        Button cancelButton = new Button("Cancelar", e -> addDialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        addDialog.add(form, buttons);
        addDialog.open();
    }

    private void eliminar(Cartera cartera) {
        Dialog eliminarDialog = new Dialog();
        Span titulo = new Span("¿Quieres eliminar la cartera?");
        Button siButton = new Button("Sí", e -> {
            carteraService.deleteCartera(cartera.getId());
            actualizarTabla();
            eliminarDialog.close();
        });
        siButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", e -> eliminarDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(siButton, cancelButton);
        eliminarDialog.add(titulo, buttons);
        eliminarDialog.open();
    }

    private void actualizarTabla() {
        List<Cartera> carteras = carteraService.getAllCarteras();
        carteraTabla.setItems(carteras);
    }
}
