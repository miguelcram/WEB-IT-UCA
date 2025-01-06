package es.uca.iw.webituca;

import es.uca.iw.webituca.Model.Usuario;
import es.uca.iw.webituca.Model.Proyecto;
import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Service.UsuarioService;
import es.uca.iw.webituca.Service.CarteraService;
import es.uca.iw.webituca.Service.ProyectoService;
import es.uca.iw.webituca.Model.Estado;

import com.github.javafaker.Faker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DatabasePopulator implements CommandLineRunner {
    
    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;
    private final CarteraService carteraService;
    private final PasswordEncoder passwordEncoder;
    
    public DatabasePopulator(UsuarioService usuarioService, ProyectoService proyectoService, CarteraService carteraService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
        this.carteraService = carteraService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        
        // Creamos admin si no hay usuarios
        if(usuarioService.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsuario("u11111111");
            admin.setNombre("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@uca.es");
            admin.setActivo(true);  //Usuario admin ya verificado
            admin.setCodigoRegistro(null);
            admin.setRol(es.uca.iw.webituca.Model.Rol.ADMIN);
            usuarioService.save(admin);
            System.out.println("Usuario Admin creado");
        }

        // Crear 3 usuarios adicionales
        if(usuarioService.count() == 1) {
            for (int i = 1; i <= 3; i++) {
                Usuario usuario = new Usuario();
                usuario.setUsuario(faker.idNumber().valid());
                usuario.setNombre(faker.name().firstName());
                usuario.setApellido1(faker.name().lastName());
                usuario.setEmail(faker.internet().emailAddress());
                usuario.setPassword(passwordEncoder.encode("password"));
                usuario.setActivo(true);
                usuario.setRol(es.uca.iw.webituca.Model.Rol.USUARIO);
                usuarioService.save(usuario);
                System.out.println("Usuario creado: " + usuario.getNombre());
            }
        }

        // Crear una única cartera
        Cartera cartera = new Cartera();
        cartera.setNombre(faker.company().name());
        cartera.setDescripcion(faker.lorem().sentence(20));
        cartera.setFechaCreacion(LocalDateTime.now().minusDays(faker.number().numberBetween(10, 100)));
        cartera.setFechaFin(LocalDateTime.now().plusDays(faker.number().numberBetween(10, 100)));
        cartera.setNumero_horas((float) faker.number().randomDouble(2, 100, 1000));
        cartera.setPresupuesto((float) faker.number().randomDouble(2, 1000, 100000));
        carteraService.createCartera(cartera);
        System.out.println("Cartera creada: " + cartera.getNombre());
            
        // Crear 5 proyectos
        if(proyectoService.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                Proyecto proyecto = new Proyecto();
                proyecto.setTitulo(faker.book().title());
                proyecto.setDescripcion(faker.lorem().sentence(10));
                proyecto.setEstado(faker.options().option(Estado.values()));
                proyecto.setPermisoGestion(faker.bool().bool());
                proyecto.setFechaInicio(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                proyecto.setFechaFin(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30)));
                proyecto.setPuntuacion1((float) faker.number().randomDouble(1, 0, 10));
                proyecto.setPuntuacion2((float) faker.number().randomDouble(1, 0, 10));
                proyecto.setPrioridad(faker.number().numberBetween(1, 5));
                proyecto.setCartera(cartera);

                // Asignar aleatoriamente un usuario al proyecto (de los 3 usuarios creados)
                List<Usuario> usuarios = usuarioService.getAllUsuarios();
                usuarios.removeIf(u -> u.getRol() == es.uca.iw.webituca.Model.Rol.ADMIN);   //Quitamos al usuario admin
                Usuario usuarioAsignado = usuarios.get(random.nextInt(usuarios.size()));
                proyecto.setUsuario(usuarioAsignado);

                // Se guarda el proyecto
                proyectoService.guardarProyecto(proyecto, null);
                System.out.println("Proyecto creado: " + proyecto.getTitulo());
            }
        }
    }
}
