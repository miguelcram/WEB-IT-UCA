package es.uca.iw.webituca.Views.VistasAdmin;

import es.uca.iw.webituca.Model.Rol;
import es.uca.iw.webituca.Model.Usuario;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.webituca.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AnonymousAllowed
@PageTitle("Administrar Usuarios")
@Route("/admin-usuarios")
public class UsuarioUpdateView extends Composite<VerticalLayout> {

    private final UsuarioService usuarioService;
    private final Grid<Usuario> grid = new Grid<>(Usuario.class);

    @Autowired
    public UsuarioUpdateView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        crearTitulo("Gestión de Usuarios");
        configurarGrid();
        getContent().add(grid);
        cargarUsuarios();  // Cargar usuarios desde la base de datos
    }

    private void crearTitulo(String titulo) {
        H1 title = new H1(titulo);
        getContent().add(title);
    }

    private void configurarGrid() {
        grid.removeAllColumns();

        // Configurar columnas del Grid
        grid.addColumn(Usuario::getNombre).setHeader("Nombre");

        // ComboBox para cambiar el rol del usuario
        grid.addComponentColumn(usuario -> {
            ComboBox<Rol> rolComboBox = new ComboBox<>("Rol");
            rolComboBox.setItems(Rol.values());
            rolComboBox.setValue(usuario.getRol());
            rolComboBox.addValueChangeListener(e -> mostrarDialogoConfirmacion(usuario, e.getValue()));
            return rolComboBox;
        }).setHeader("Rol");

        // // Columna con el icono de editar
        // grid.addComponentColumn(usuario -> {
        //     Icon editIcon = VaadinIcon.EDIT.create();
        //     editIcon.addClickListener(event -> mostrarDialogoEditar(usuario));
        //     return editIcon;
        // }).setHeader("Acciones");


        grid.addComponentColumn(usuario -> {
            Button editarBtn = new Button(VaadinIcon.EDIT.create());
            editarBtn.addClickListener(event -> mostrarDialogoEditar(usuario));
            return editarBtn;
        }).setHeader("Acciones");

        // Columna con el botón de eliminar
        grid.addComponentColumn(usuario -> {
            Button eliminarBtn = new Button("Eliminar", event -> mostrarDialogoEliminar(usuario));
            eliminarBtn.addClassName("danger"); // Estilo para resaltar el botón como acción peligrosa
            return eliminarBtn;
        }).setHeader("Eliminar");
    }

    // Mostrar diálogo de confirmación para eliminar el usuario
    private void mostrarDialogoEliminar(Usuario usuario) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
    
        VerticalLayout contenido = new VerticalLayout();
        contenido.add("¿Estás seguro de que deseas eliminar al usuario " + usuario.getNombre() + "?");
    
        Button confirmarBtn = new Button("Eliminar", event -> {
            try {
                usuarioService.deleteUsuario(usuario.getId());
                grid.getDataProvider().refreshAll(); // Refrescar el grid
                Notification.show("Usuario eliminado con éxito.");
            } catch (IllegalStateException e) {
                Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE); // Mostrar el mensaje de error
            }
            dialog.close();
        });
    
        Button cancelarBtn = new Button("Cancelar", event -> dialog.close());
        HorizontalLayout botones = new HorizontalLayout(confirmarBtn, cancelarBtn);
        contenido.add(botones);
    
        dialog.add(contenido);
        dialog.open();
    }





    // Cargar usuarios desde el servicio
    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        grid.setItems(usuarios);
    }

    // Mostrar diálogo con los datos del usuario para modificar
    private void mostrarDialogoEditar(Usuario usuario) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
    
        FormLayout formulario = new FormLayout();
    
        // Crear los campos de formulario con los valores actuales del usuario
        TextField nombreField = new TextField("Nombre");
        nombreField.setValue(usuario.getNombre());
    
        TextField apellido1Field = new TextField("Apellido1");
        apellido1Field.setValue(usuario.getApellido1());
    
        TextField apellido2Field = new TextField("Apellido2");
        apellido2Field.setValue(usuario.getApellido2());
    
        TextField emailField = new TextField("Email");
        emailField.setValue(usuario.getEmail());
    
        TextField telefonoField = new TextField("Telefono");
        telefonoField.setValue(usuario.getTelefono());
    
        formulario.add(nombreField, apellido1Field, apellido2Field, telefonoField, emailField);
    
        // Botones de "Guardar" y "Cancelar"
        Button guardarBtn = new Button("Guardar", event -> {
            usuario.setNombre(nombreField.getValue());
            usuario.setApellido1(apellido1Field.getValue());
            usuario.setApellido2(apellido2Field.getValue());
            usuario.setEmail(emailField.getValue());
            usuario.setTelefono(telefonoField.getValue());
    
            // Actualizar los datos del usuario en la base de datos
            usuarioService.saveOrUpdateUsuario(usuario);
            grid.setItems(usuarioService.getAllUsuarios()); // Refrescar el grid
            dialog.close();
            Notification.show("Usuario actualizado con éxito.");
        });
    
        Button cancelarBtn = new Button("Cancelar", event -> dialog.close());
        HorizontalLayout botones = new HorizontalLayout(guardarBtn, cancelarBtn);
        botones.setJustifyContentMode(HorizontalLayout.JustifyContentMode.CENTER);
    
        dialog.add(formulario, botones);
        dialog.open();
    }

    // Mostrar diálogo de confirmación para cambiar el rol
    private void mostrarDialogoConfirmacion(Usuario usuario, Rol nuevoRol) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        VerticalLayout contenido = new VerticalLayout();
        contenido.add("¿Estás seguro de que quieres cambiar el rol de " + usuario.getNombre() + " a " + nuevoRol + "?");

        Button confirmarBtn = new Button("Confirmar", event -> {
            Usuario usuarioActualizado = usuarioService.updateRol(usuario.getId(), nuevoRol);
            if (usuarioActualizado != null) {
                grid.getDataProvider().refreshAll();
                Notification.show("Rol actualizado a " + nuevoRol);
            } else {
                Notification.show("Error al actualizar el rol.");
            }
            dialog.close();
        });

        Button cancelarBtn = new Button("Cancelar", event -> dialog.close());
        HorizontalLayout botones = new HorizontalLayout(confirmarBtn, cancelarBtn);
        contenido.add(botones);

        dialog.add(contenido);
        dialog.open();
    }
}