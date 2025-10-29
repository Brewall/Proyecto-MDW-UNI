package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.EventoService;
import com.example.app.service.interfaces.CuotaService;
import com.example.app.service.interfaces.UsuarioService;
import com.example.app.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/apuestas")
public class ApuestaWebController {

    @Autowired
    private ApuestaService apuestaService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private UsuarioService usuarioService;

    // LISTAR APUESTAS CON FILTROS MEJORADOS
    @GetMapping
    public String listarApuestas(
            HttpSession session,
            Model model,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "deporte", required = false) String deporte) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // DEBUG: Mostrar parámetros recibidos
        System.out.println("=== FILTROS RECIBIDOS ===");
        System.out.println("Estado: " + estado);
        System.out.println("Deporte: " + deporte);

        // Obtener TODAS las apuestas del usuario primero
        List<com.example.app.model.Apuesta> todasLasApuestas = apuestaService.findByUsuarioId(usuario.getId());
        List<com.example.app.model.Apuesta> apuestasFiltradas = todasLasApuestas;

        System.out.println("Total apuestas: " + todasLasApuestas.size());

        // Aplicar filtro por ESTADO si se especifica
        if (estado != null && !estado.trim().isEmpty()) {
            apuestasFiltradas = apuestasFiltradas.stream()
                    .filter(apuesta -> {
                        boolean coincide = estado.equals(apuesta.getEstado());
                        if (coincide) {
                            System.out.println("Apuesta coincide con estado " + estado + ": " + apuesta.getEvento().getNombreEvento());
                        }
                        return coincide;
                    })
                    .collect(Collectors.toList());
            System.out.println("Después de filtrar por estado: " + apuestasFiltradas.size());
        }

        // Aplicar filtro por DEPORTE si se especifica
        if (deporte != null && !deporte.trim().isEmpty()) {
            apuestasFiltradas = apuestasFiltradas.stream()
                    .filter(apuesta -> {
                        String deporteEvento = apuesta.getEvento().getDeporte();
                        boolean coincide = deporte.equals(deporteEvento);
                        if (coincide) {
                            System.out.println("Apuesta coincide con deporte " + deporte + ": " + apuesta.getEvento().getNombreEvento() + " (" + deporteEvento + ")");
                        }
                        return coincide;
                    })
                    .collect(Collectors.toList());
            System.out.println("Después de filtrar por deporte: " + apuestasFiltradas.size());
        }

        System.out.println("Apuestas finales: " + apuestasFiltradas.size());
        System.out.println("=========================");

        // Obtener lista de deportes únicos de las apuestas REALES del usuario
        List<String> deportesUnicos = todasLasApuestas.stream()
                .map(apuesta -> apuesta.getEvento().getDeporte())
                .distinct()
                .collect(Collectors.toList());

        // Calcular estadísticas
        long totalApuestas = apuestasFiltradas.size();
        long apuestasGanadas = apuestasFiltradas.stream().filter(a -> "GANADA".equals(a.getEstado())).count();
        long apuestasPerdidas = apuestasFiltradas.stream().filter(a -> "PERDIDA".equals(a.getEstado())).count();
        long apuestasPendientes = apuestasFiltradas.stream().filter(a -> "PENDIENTE".equals(a.getEstado())).count();

        model.addAttribute("usuario", usuario);
        model.addAttribute("apuestas", apuestasFiltradas);
        model.addAttribute("totalApuestas", totalApuestas);
        model.addAttribute("apuestasGanadas", apuestasGanadas);
        model.addAttribute("apuestasPerdidas", apuestasPerdidas);
        model.addAttribute("apuestasPendientes", apuestasPendientes);
        model.addAttribute("deportes", deportesUnicos); // Usar deportes reales del usuario

        return "apuestas";
    }

    // REALIZAR APUESTA (desde dashboard)
    @PostMapping("/realizar")
    public String realizarApuesta(
            @RequestParam("cuotaId") Integer cuotaId,
            @RequestParam("monto") Double monto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            apuestaService.realizarApuesta(usuario.getId(), cuotaId, monto);
            model.addAttribute("exito", "✅ Apuesta realizada correctamente por $" + monto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "❌ " + e.getMessage());
        }

        // Recargar datos para el dashboard
        model.addAttribute("usuario", usuarioService.findById(usuario.getId()).orElse(usuario));
        model.addAttribute("eventos", eventoService.findEventosDisponiblesParaApuestas());
        return "redirect:/dashboard";
    }

    // CANCELAR APUESTA
    @PostMapping("/{id}/cancelar")
    public String cancelarApuesta(
            @PathVariable("id") Integer apuestaId,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            apuestaService.cancelarApuesta(apuestaId);
            model.addAttribute("exito", "✅ Apuesta cancelada correctamente");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "❌ " + e.getMessage());
        }

        return "redirect:/apuestas";
    }
}