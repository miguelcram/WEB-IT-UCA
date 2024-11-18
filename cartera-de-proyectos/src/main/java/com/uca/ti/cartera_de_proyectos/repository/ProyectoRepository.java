package com.uca.ti.cartera_de_proyectos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uca.ti.cartera_de_proyectos.model.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    
}
