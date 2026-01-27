package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.EventoService;
import com.example.app.service.interfaces.CuotaService;
import com.example.app.service.interfaces.TransaccionService;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ApuestaService apuestaService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Usuario usuario = usuarioService.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<com.example.app.model.Evento> eventos = eventoService.findEventosDisponiblesParaApuestas();

        Map<Integer, List<com.example.app.model.Cuota>> cuotasPorEvento = new HashMap<>();
        if (eventos != null) {
            for (com.example.app.model.Evento evento : eventos) {
                List<com.example.app.model.Cuota> cuotas = cuotaService.findCuotasDisponiblesByEvento(evento.getId());
                cuotasPorEvento.put(evento.getId(), cuotas);
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("eventos", eventos);
        model.addAttribute("cuotasPorEvento", cuotasPorEvento);
        model.addAttribute("apuestasRecientes", apuestaService.findByUsuarioIdWithEventoAndCuota(usuario.getId()).stream().limit(5).toList());
        model.addAttribute("apuestasPendientes", apuestaService.findApuestasPendientesByUsuario(usuario.getId()));

        try {
            model.addAttribute("gananciasTotales", transaccionService.calcularGananciasTotalesUsuario(usuario.getId()));
        } catch (Exception e) {
            model.addAttribute("gananciasTotales", 0.0);
        }

        return "dashboard";
    }
}
