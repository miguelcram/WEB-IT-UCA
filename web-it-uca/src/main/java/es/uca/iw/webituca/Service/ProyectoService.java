package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Repository.ProyectoRepository;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

       /**
     * Guarda un proyecto en la base de datos y lo asocia a un usuario.
     * 
     * @param nombre El nombre del proyecto.
     * @param descripcion La descripción del proyecto.
     * @param usuario El usuario dueño del proyecto.
     * @return El proyecto guardado con el dueño asignado.
     */
        public Proyecto guardarProyecto(String nombre, String descripcion, Usuario usuario) {
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setUsuario(usuario); // Asigna el usuario como dueño del proyecto.
        return proyectoRepository.save(proyecto);
    }

    public long count() {
        return proyectoRepository.count();
    }

    // public List<Proyecto> listarProyectos() {
    //     return proyectoRepository.findAll();
    // }
    

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
}

}


