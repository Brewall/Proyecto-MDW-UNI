package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación y registro de usuarios.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión correctamente");
        }
        return "login";
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

        logger.info("=== REGISTRO: Recibida petición de registro ===");
        logger.info("Usuario: {}, Correo: {}", nombreUsuario, correo);

        try {
            Usuario usuario = new Usuario(nombreUsuario, correo, password);
            Usuario guardado = usuarioService.save(usuario);
            logger.info("Usuario registrado exitosamente con ID: {}", guardado.getId());
            model.addAttribute("exito", "Usuario registrado correctamente. ¡Inicia sesión!");
            return "login";
        } catch (IllegalArgumentException e) {
            logger.error("Error en registro: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("nombreUsuario", nombreUsuario);
            model.addAttribute("correo", correo);
            return "registro";
        } catch (Exception e) {
            logger.error("Error inesperado en registro: ", e);
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            return "registro";
        }
    }
}
