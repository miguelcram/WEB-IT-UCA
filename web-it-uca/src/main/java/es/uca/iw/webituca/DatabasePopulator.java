package es.uca.iw.webituca;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Service.UsuarioService;
import es.uca.iw.webituca.Service.ProyectoService;

import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {
    
    private final UsuarioService userService;
    private final ProyectoService proyectoService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public DatabasePopulator(UsuarioService userService, ProyectoService proyectoService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.proyectoService = proyectoService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        
        // Creamos admin si no hay usuarios
        if(userService.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsuario("u1111111111");
            admin.setNombre("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@uca.es");
            //Usuario admin ya verificado
            admin.setActivo(true);
            admin.setCodigoRegistro(null);
            userService.save(admin);
            System.out.println("Admin created");
        }

        // Crear proyectos con faker si no existe ninguno
        if(proyectoService.count() == 0) {
            for (int i = 1; i <= 5; i++) {
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
