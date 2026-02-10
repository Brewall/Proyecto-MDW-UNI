package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.TransaccionService;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transacciones")
public class TransaccionWebController {

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarTransacciones(
            Authentication authentication,
            Model model,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFin", required = false) String fechaFin) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        List<com.example.app.model.Transaccion> todasLasTransacciones = transaccionService.findByUsuarioId(usuario.getId());
        List<com.example.app.model.Transaccion> transaccionesFiltradas = todasLasTransacciones;

        if (tipo != null && !tipo.trim().isEmpty()) {
            transaccionesFiltradas = transaccionesFiltradas.stream()
                    .filter(transaccion -> tipo.equals(transaccion.getTipo()))
                    .collect(Collectors.toList());
        }

        if (fechaInicio != null && !fechaInicio.trim().isEmpty() &&
                fechaFin != null && !fechaFin.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime inicio = LocalDate.parse(fechaInicio, formatter).atStartOfDay();
                LocalDateTime fin = LocalDate.parse(fechaFin, formatter).atTime(23, 59, 59);

                transaccionesFiltradas = transaccionesFiltradas.stream()
                        .filter(t -> !t.getFechaTransaccion().isBefore(inicio) &&
                                    !t.getFechaTransaccion().isAfter(fin))
                        .collect(Collectors.toList());
            } catch (Exception e) {
            }
        }

        java.util.Set<String> apuestasCanceladas = transaccionesFiltradas.stream()
                .filter(t -> t.getDescripcion() != null && t.getDescripcion().startsWith("Devolución apuesta cancelada #"))
                .map(t -> t.getDescripcion().replace("Devolución apuesta cancelada #", ""))
                .collect(Collectors.toSet());

        Double totalDepositos = transaccionesFiltradas.stream()
                .filter(t -> "DEPOSITO".equals(t.getTipo()))
                .filter(t -> t.getDescripcion() == null || !t.getDescripcion().startsWith("Devolución apuesta cancelada"))
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        Double totalGanancias = transaccionesFiltradas.stream()
                .filter(t -> "GANANCIA".equals(t.getTipo()))
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        Double totalApuestas = transaccionesFiltradas.stream()
                .filter(t -> "APUESTA".equals(t.getTipo()))
                .filter(t -> {
                    if (t.getDescripcion() == null) return true;
                    String desc = t.getDescripcion();
                    for (String idCancelada : apuestasCanceladas) {
                        if (desc.endsWith("#" + idCancelada) || desc.contains("apuesta #" + idCancelada)) {
                            return false;
                        }
                    }
                    return true;
                })
                .mapToDouble(t -> Math.abs(t.getMonto()))
                .sum();

        Double totalRetiros = transaccionesFiltradas.stream()
                .filter(t -> "RETIRO".equals(t.getTipo()))
                .mapToDouble(t -> Math.abs(t.getMonto()))
                .sum();

        Double balanceTotal = transaccionesFiltradas.stream()
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        List<com.example.app.model.Transaccion> todasOrdenadas = new ArrayList<>(todasLasTransacciones);
        todasOrdenadas.sort(Comparator.comparing(com.example.app.model.Transaccion::getFechaTransaccion));
        
        double saldoAcumulado = 0.0;
        java.util.Map<Integer, Double> saldosPorTransaccion = new java.util.HashMap<>();
        for (com.example.app.model.Transaccion t : todasOrdenadas) {
            saldoAcumulado += t.getMonto();
            saldosPorTransaccion.put(t.getId(), saldoAcumulado);
        }
        
        List<Double> saldosPosteriores = new ArrayList<>();
        for (com.example.app.model.Transaccion t : transaccionesFiltradas) {
            saldosPosteriores.add(saldosPorTransaccion.getOrDefault(t.getId(), 0.0));
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("transacciones", transaccionesFiltradas);
        model.addAttribute("saldosPosteriores", saldosPosteriores);
        model.addAttribute("totalDepositos", totalDepositos);
        model.addAttribute("totalGanancias", totalGanancias);
        model.addAttribute("totalApuestas", totalApuestas);
        model.addAttribute("totalRetiros", totalRetiros);
        model.addAttribute("balanceTotal", balanceTotal);

        return "transacciones";
    }
}
