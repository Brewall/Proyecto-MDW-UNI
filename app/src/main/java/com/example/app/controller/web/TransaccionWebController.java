package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transacciones")
public class TransaccionWebController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public String listarTransacciones(
            HttpSession session,
            Model model,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFin", required = false) String fechaFin) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // DEBUG: Mostrar parámetros recibidos
        System.out.println("=== FILTROS TRANSACCIONES ===");
        System.out.println("Tipo: " + tipo);
        System.out.println("Fecha Inicio: " + fechaInicio);
        System.out.println("Fecha Fin: " + fechaFin);

        // Obtener TODAS las transacciones del usuario primero
        List<com.example.app.model.Transaccion> todasLasTransacciones = transaccionService.findByUsuarioId(usuario.getId());
        List<com.example.app.model.Transaccion> transaccionesFiltradas = todasLasTransacciones;

        System.out.println("Total transacciones: " + todasLasTransacciones.size());

        // Mostrar tipos de transacciones existentes
        List<String> tiposExistentes = todasLasTransacciones.stream()
                .map(t -> t.getTipo())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Tipos existentes: " + tiposExistentes);

        // Aplicar filtro por TIPO si se especifica
        if (tipo != null && !tipo.trim().isEmpty()) {
            transaccionesFiltradas = transaccionesFiltradas.stream()
                    .filter(transaccion -> {
                        boolean coincide = tipo.equals(transaccion.getTipo());
                        if (coincide) {
                            System.out.println("Transacción coincide con tipo " + tipo + ": " +
                                    transaccion.getDescripcion() + " (" + transaccion.getTipo() + ")");
                        }
                        return coincide;
                    })
                    .collect(Collectors.toList());
            System.out.println("Después de filtrar por tipo: " + transaccionesFiltradas.size());
        }

        // Aplicar filtro de FECHAS si se especifican
        if (fechaInicio != null && !fechaInicio.trim().isEmpty() &&
                fechaFin != null && !fechaFin.trim().isEmpty()) {

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime inicio = LocalDate.parse(fechaInicio, formatter).atStartOfDay();
                LocalDateTime fin = LocalDate.parse(fechaFin, formatter).atTime(23, 59, 59);

                transaccionesFiltradas = transaccionesFiltradas.stream()
                        .filter(t -> {
                            boolean dentroRango = !t.getFechaTransaccion().isBefore(inicio) &&
                                    !t.getFechaTransaccion().isAfter(fin);
                            if (dentroRango) {
                                System.out.println("Transacción en rango: " +
                                        t.getDescripcion() + " - " + t.getFechaTransaccion());
                            }
                            return dentroRango;
                        })
                        .collect(Collectors.toList());
                System.out.println("Después de filtrar por fechas: " + transaccionesFiltradas.size());
            } catch (Exception e) {
                System.out.println("Error al parsear fechas: " + e.getMessage());
            }
        }

        System.out.println("Transacciones finales: " + transaccionesFiltradas.size());
        System.out.println("=============================");

        // Calcular totales
        Double totalDepositos = transaccionesFiltradas.stream()
                .filter(t -> "DEPOSITO".equals(t.getTipo()))
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        Double totalGanancias = transaccionesFiltradas.stream()
                .filter(t -> "GANANCIA".equals(t.getTipo()))
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        Double totalApuestas = transaccionesFiltradas.stream()
                .filter(t -> "APUESTA".equals(t.getTipo()))
                .mapToDouble(t -> Math.abs(t.getMonto()))
                .sum();

        Double totalRetiros = transaccionesFiltradas.stream()
                .filter(t -> "RETIRO".equals(t.getTipo()))
                .mapToDouble(t -> Math.abs(t.getMonto()))
                .sum();

        // Calcular balance total
        Double balanceTotal = transaccionesFiltradas.stream()
                .mapToDouble(com.example.app.model.Transaccion::getMonto)
                .sum();

        // Calcular saldos posteriores (simulado)
        Double[] saldosPosteriores = new Double[transaccionesFiltradas.size()];
        Double saldoAcumulado = 0.0;
        for (int i = 0; i < transaccionesFiltradas.size(); i++) {
            saldoAcumulado += transaccionesFiltradas.get(i).getMonto();
            saldosPosteriores[i] = saldoAcumulado;
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("transacciones", transaccionesFiltradas);
        model.addAttribute("totalDepositos", totalDepositos);
        model.addAttribute("totalGanancias", totalGanancias);
        model.addAttribute("totalApuestas", totalApuestas);
        model.addAttribute("totalRetiros", totalRetiros);
        model.addAttribute("balanceTotal", balanceTotal);
        model.addAttribute("saldosPosteriores", saldosPosteriores);

        return "transacciones";
    }
}
