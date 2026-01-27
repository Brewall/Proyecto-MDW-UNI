package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.EventoService;
import com.example.app.service.interfaces.CuotaService;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String listarApuestas(
            Authentication authentication,
            Model model,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "deporte", required = false) String deporte) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        List<com.example.app.model.Apuesta> todasLasApuestas = apuestaService.findByUsuarioIdWithEventoAndCuota(usuario.getId());
        List<com.example.app.model.Apuesta> apuestasFiltradas = todasLasApuestas;

        if (estado != null && !estado.trim().isEmpty()) {
            apuestasFiltradas = apuestasFiltradas.stream()
                    .filter(apuesta -> estado.equals(apuesta.getEstado()))
                    .collect(Collectors.toList());
        }

        if (deporte != null && !deporte.trim().isEmpty()) {
            apuestasFiltradas = apuestasFiltradas.stream()
                    .filter(apuesta -> deporte.equals(apuesta.getEvento().getDeporte()))
                    .collect(Collectors.toList());
        }

        List<String> deportesUnicos = todasLasApuestas.stream()
                .map(apuesta -> apuesta.getEvento().getDeporte())
                .distinct()
                .collect(Collectors.toList());

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
        model.addAttribute("deportes", deportesUnicos);

        return "apuestas";
    }

    @PostMapping("/realizar")
    public String realizarApuesta(
            @RequestParam("cuotaId") Integer cuotaId,
            @RequestParam("monto") Double monto,
            Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        if ("INHABILITADO".equals(usuario.getEstado())) {
            redirectAttributes.addFlashAttribute("error", "Tu cuenta está inhabilitada. No puedes realizar apuestas.");
            return "redirect:/dashboard";
        }

        try {
            apuestaService.realizarApuesta(usuario.getId(), cuotaId, monto);
            redirectAttributes.addFlashAttribute("exito", "Apuesta realizada correctamente por $" + monto);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarApuesta(
            @PathVariable("id") Integer apuestaId,
            Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        usuarioService.getUsuarioAutenticado(authentication);

        try {
            apuestaService.cancelarApuesta(apuestaId);
            redirectAttributes.addFlashAttribute("exito", "Apuesta cancelada correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/apuestas";
    }
}
