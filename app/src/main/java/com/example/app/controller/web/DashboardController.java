package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.EventoService;
import com.example.app.service.interfaces.CuotaService;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Cargar datos para el dashboard
        List<com.example.app.model.Evento> eventos = eventoService.findEventosDisponiblesParaApuestas();

        // DEBUG: Verificar qué eventos se están cargando
        System.out.println("=== DEBUG DASHBOARD ===");
        System.out.println("Eventos encontrados: " + (eventos != null ? eventos.size() : 0));
        if (eventos != null) {
            for (com.example.app.model.Evento evento : eventos) {
                System.out.println("Evento: " + evento.getNombreEvento() + " (ID: " + evento.getId() + ")");
                // Cargar cuotas para cada evento
                List<com.example.app.model.Cuota> cuotas = cuotaService.findCuotasDisponiblesByEvento(evento.getId());
                System.out.println("Cuotas para " + evento.getNombreEvento() + ": " + cuotas.size());
                for (com.example.app.model.Cuota cuota : cuotas) {
                    System.out.println("  - " + cuota.getDescripcion() + " (" + cuota.getValor() + ")");
                }
            }
        }
        System.out.println("======================");

        // Preparar datos para la vista
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
        model.addAttribute("apuestasRecientes", apuestaService.findByUsuarioId(usuario.getId()).stream().limit(5).toList());
        model.addAttribute("apuestasPendientes", apuestaService.findApuestasPendientesByUsuario(usuario.getId()));

        try {
            model.addAttribute("gananciasTotales", transaccionService.calcularGananciasTotalesUsuario(usuario.getId()));
        } catch (Exception e) {
            model.addAttribute("gananciasTotales", 0.0);
        }

        return "dashboard";
    }
}
