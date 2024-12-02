package es.uca.iw.webituca.Service;

import java.util.ArrayList;
import java.util.List;

import es.uca.iw.webituca.Model.Proyecto;

public class ProyectoService {
    public List<Proyecto> listarProyectos(String username) {
        List<Proyecto> proyectos = new ArrayList<>();

        //Prueba
        proyectos.add(new Proyecto("Proyecto 1", true, username != null)); // Solo logados gestionan
        proyectos.add(new Proyecto("Proyecto 2", true, false));
        proyectos.add(new Proyecto("Proyecto 3", false, false)); // Proyecto inactivo
        proyectos.add(new Proyecto("Proyecto 4", true, username != null));

        return proyectos;
    }
}
