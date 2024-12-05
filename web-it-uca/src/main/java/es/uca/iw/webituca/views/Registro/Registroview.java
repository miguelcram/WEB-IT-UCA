package es.uca.iw.webituca.views.Registro;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.webituca.views.MainLayout;
import jakarta.validation.constraints.Email;


@PageTitle("Registro")
@Route(value = "registro",layout = MainLayout.class)
public class Registroview extends Composite<VerticalLayout>{

    private final TextField nombre = new TextField("Nombre");
    private final TextField apellido1 = new TextField("Apellido1");
    private final TextField apellido2 = new TextField("Apellidos2");
    private final EmailField email = new EmailField("Email");
    private final TextField telefono = new TextField("Telefono");
    private final PasswordField password = new PasswordField("Contraseña");
    private final PasswordField repite_password = new PasswordField("Repite Contraseña");




    public Registroview() {
        //getContent().add(new RegistroForm());
        H1 h1 = new H1("Registro");
        FormLayout form = new FormLayout();
        form.add(nombre,apellido1,apellido2,email,telefono,password,repite_password);



        Button guardarButton = new Button("Guardar", event -> {
            // Aquí iría la lógica para guardar los datos en la base de datos
            // Por simplicidad, vamos a mostrar los datos en la consola
            System.out.println("Nombre: " + nombre.getValue());
            System.out.println("Apellido1: " + apellido1.getValue());
            System.out.println("Apellido2: " + apellido2.getValue());
            System.out.println("Email: " + email.getValue());
            System.out.println("Telefono: " + telefono.getValue());
            System.out.println("Contraseña: " + password.getValue());
            System.out.println("Repite Contraseña: " + repite_password.getValue());
        });













        getContent().add(h1);
        getContent().add(form);
        getContent().add(guardarButton);
    }
    
}
