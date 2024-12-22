package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Repository.ProyectoRepository;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public long count() {
        return proyectoRepository.count();
    }

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }
    
}
