package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.findByCorreo(correo);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            session.setAttribute("usuario", usuarioOpt.get());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombreUsuario,
            @RequestParam String correo,
            @RequestParam String password,
            Model model) {

        try {
            Usuario usuario = new Usuario(nombreUsuario, correo, password);
            usuarioService.save(usuario);
            model.addAttribute("exito", "Usuario registrado correctamente");
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
