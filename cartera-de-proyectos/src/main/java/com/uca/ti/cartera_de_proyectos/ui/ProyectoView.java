package com.uca.ti.cartera_de_proyectos.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.uca.ti.cartera_de_proyectos.model.Proyecto;
import com.uca.ti.cartera_de_proyectos.service.ProyectoService;
//import org.springframework.beans.factory.annotation.Autowired;

@Route
public class ProyectoView extends VerticalLayout {
    
    private final ProyectoService proyectoService;
    private final Grid<Proyecto> grid = new Grid<>(Proyecto.class);
    private final TextField nombre = new TextField("Nombre");
    private final TextField descripcion = new TextField("Descripción");
    private final TextField estado = new TextField("Estado");

    //@Autowired
    public ProyectoView(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;

        Button saveButton = new Button("Guardar", e -> saveProyecto());
        add(nombre, descripcion, estado, saveButton, grid);

        updateGrid();
    }

    private void saveProyecto() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre.getValue());
        proyecto.setDescripcion(descripcion.getValue());
        proyecto.setEstado(estado.getValue());

        proyectoService.saveProyecto(proyecto);
        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(proyectoService.getAllProyectos());
    }
}
