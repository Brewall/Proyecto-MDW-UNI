package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.UsuarioService;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApuestaService apuestaService;

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Actualizar usuario desde BD
        Usuario usuarioActualizado = usuarioService.findById(usuario.getId()).orElse(usuario);
        session.setAttribute("usuario", usuarioActualizado);

        // Estadísticas para el perfil
        List<com.example.app.model.Apuesta> apuestasUsuario = apuestaService.findByUsuarioId(usuario.getId());
        long totalApuestas = apuestasUsuario.size();
        long apuestasGanadas = apuestasUsuario.stream().filter(a -> "GANADA".equals(a.getEstado())).count();
        long apuestasPerdidas = apuestasUsuario.stream().filter(a -> "PERDIDA".equals(a.getEstado())).count();

        model.addAttribute("usuario", usuarioActualizado);
        model.addAttribute("totalApuestas", totalApuestas);
        model.addAttribute("apuestasGanadas", apuestasGanadas);
        model.addAttribute("apuestasPerdidas", apuestasPerdidas);

        try {
            model.addAttribute("gananciasTotales", transaccionService.calcularGananciasTotalesUsuario(usuario.getId()));
        } catch (Exception e) {
            model.addAttribute("gananciasTotales", 0.0);
        }

        return "perfil";
    }

    @PostMapping("/depositar")
    public String depositarSaldo(
            @RequestParam("monto") Double monto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuarioActualizado = usuarioService.depositarSaldo(usuario.getId(), monto);
            session.setAttribute("usuario", usuarioActualizado);

            // Registrar transacción
            transaccionService.registrarDeposito(usuario.getId(), monto, "Depósito desde perfil");

            model.addAttribute("exito", "Saldo depositado correctamente: $" + monto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recargar datos
        return mostrarPerfil(session, model);
    }

    @PostMapping("/retirar")
    public String retirarSaldo(
            @RequestParam("monto") Double monto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuarioActualizado = usuarioService.retirarSaldo(usuario.getId(), monto);
            session.setAttribute("usuario", usuarioActualizado);

            // Registrar transacción
            transaccionService.registrarRetiro(usuario.getId(), monto, "Retiro desde perfil");

            model.addAttribute("exito", "Saldo retirado correctamente: $" + monto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recargar datos
        return mostrarPerfil(session, model);
    }
}
