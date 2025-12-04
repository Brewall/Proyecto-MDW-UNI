package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.UsuarioService;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String mostrarPerfil(Authentication authentication, Model model) {
        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        // Estadísticas para el perfil
        List<com.example.app.model.Apuesta> apuestasUsuario = apuestaService.findByUsuarioId(usuario.getId());
        long totalApuestas = apuestasUsuario.size();
        long apuestasGanadas = apuestasUsuario.stream().filter(a -> "GANADA".equals(a.getEstado())).count();
        long apuestasPerdidas = apuestasUsuario.stream().filter(a -> "PERDIDA".equals(a.getEstado())).count();

        model.addAttribute("usuario", usuario);
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
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        // INHABILITADO users can still deposit
        try {
            usuarioService.depositarSaldo(usuario.getId(), monto);

            // Registrar transacción
            transaccionService.registrarDeposito(usuario.getId(), monto, "Depósito desde perfil");

            model.addAttribute("exito", "Saldo depositado correctamente: $" + monto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recargar datos
        return mostrarPerfil(authentication, model);
    }

    @PostMapping("/retirar")
    public String retirarSaldo(
            @RequestParam("monto") Double monto,
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        // INHABILITADO users can still withdraw
        try {
            usuarioService.retirarSaldo(usuario.getId(), monto);

            // Registrar transacción
            transaccionService.registrarRetiro(usuario.getId(), monto, "Retiro desde perfil");

            model.addAttribute("exito", "Saldo retirado correctamente: $" + monto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recargar datos
        return mostrarPerfil(authentication, model);
    }
}
