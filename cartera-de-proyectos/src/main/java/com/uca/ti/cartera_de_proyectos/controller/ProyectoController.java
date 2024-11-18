package com.uca.ti.cartera_de_proyectos.controller;

import com.uca.ti.cartera_de_proyectos.model.Proyecto;
import com.uca.ti.cartera_de_proyectos.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
    @Autowired
    private ProyectoService proyectoService;

    @GetMapping
    public List<Proyecto> getProyectos() {
        return proyectoService.getAllProyectos();
    }

    @PostMapping
    public Proyecto addProyecto(@RequestBody Proyecto proyecto) {
        return proyectoService.saveProyecto(proyecto);
    }
}
