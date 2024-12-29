package es.uca.iw.webituca.Controller;

import es.uca.iw.webituca.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @GetMapping("/activar")
    public String activarCuenta(@RequestParam("email") String email, @RequestParam("codigo") String codigoRegistro, Model model) {

        boolean cuentaActivada = usuarioService.activarUsuario(email, codigoRegistro);

        if (cuentaActivada) {
            model.addAttribute("mensaje", "Cuenta activada exitosamente.");
            System.out.println("Cuenta activada con éxito para: " + email);
        } else {
            model.addAttribute("mensaje", "El código de activación es inválido o ha expirado.");
            System.out.println("Falló la activación para: " + email);
        }
        return "redirect:/activar?mensaje=" + model.getAttribute("mensaje");
    }
}

