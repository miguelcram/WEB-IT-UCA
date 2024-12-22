package es.uca.iw.webituca;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.UsuarioService;
import es.uca.iw.webituca.Service.ProyectoService;

import com.github.javafaker.Faker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {
    
    private final UsuarioService userService;
    private final ProyectoService proyectoService;
    
    public DatabasePopulator(UsuarioService userService, ProyectoService proyectoService) {
        this.userService = userService;
        this.proyectoService = proyectoService;
    }
    
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        
        // Creamos admin si no hay usuarios
        if(userService.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("admin");
            admin.setPassword("admin");
            admin.setEmail("admin@uca.es");
            userService.save(admin);
            System.out.println("Admin created");
        }

        // Crear proyectos si no existen
        if(proyectoService.count() == 0) {
            for (int i = 1; i <= 20; i++) {
                Proyecto proyecto = new Proyecto();
                proyecto.setTitulo(faker.book().title());
                proyecto.setDescripcion(faker.lorem().sentence(10));
                proyecto.setActivo(faker.bool().bool());
                proyectoService.save(proyecto);
                System.out.println("Proyecto creado: " + proyecto.getTitulo());
            }
        }
    }
}
