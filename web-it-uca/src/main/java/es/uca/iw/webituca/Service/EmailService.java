package es.uca.iw.webituca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.uca.iw.webituca.Model.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.host:localhost}")
    private String serverHost;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String getServerUrl() {
        return "http://" + serverHost + ":" + serverPort + "/";
    }

    public boolean enviarCorreoRegistro(Usuario user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Bienvenido a WebITUca";
        String activationUrl = getServerUrl() + "activar?email=" + user.getEmail() + "&codigo=" + user.getCodigoRegistro();
        String body = "Hola " + user.getNombre() + ",\n\n"
                + "Gracias por registrarte en nuestra web.  "
                + "Para verificar tu cuenta, haz clic en el siguiente enlace:\n\n" 
                + activationUrl + "\n\n"
                + "Si no reconoces este registro, ignora este correo.";
        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean enviarEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom(defaultMail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
