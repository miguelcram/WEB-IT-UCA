package com.uca.ti.cartera_de_proyectos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uca.ti.cartera_de_proyectos.model.Proyecto;
import com.uca.ti.cartera_de_proyectos.repository.ProyectoRepository;

@Service
public class ProyectoService {
    @Autowired
    private ProyectoRepository proyectoRepository;
    public List<Proyecto> getAllProyectos() {
        return proyectoRepository.findAll();
    }

    public Proyecto saveProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }
}
