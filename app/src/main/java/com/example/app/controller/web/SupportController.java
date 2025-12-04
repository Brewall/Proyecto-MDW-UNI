package com.example.app.controller.web;

import com.example.app.model.Reclamo;
import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ReclamoService;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for SUPPORT role functionalities.
 * Allows managing users and responding to claims.
 */
@Controller
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReclamoService reclamoService;

    /**
     * Gets the authenticated support user.
     */
    private Usuario getSupportUser(Authentication authentication) {
        String correo = authentication.getName();
        return usuarioService.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // ==================== DASHBOARD ====================

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Usuario supportUser = getSupportUser(authentication);

        // General statistics
        List<Usuario> allUsers = usuarioService.findAll();
        List<Reclamo> allClaims = reclamoService.findAll();

        long totalUsuarios = allUsers.stream().filter(u -> "USER".equals(u.getRol())).count();
        long usuariosActivos = allUsers.stream()
                .filter(u -> "USER".equals(u.getRol()) && "ACTIVO".equals(u.getEstado())).count();
        long usuariosInhabilitados = allUsers.stream()
                .filter(u -> "USER".equals(u.getRol()) && "INHABILITADO".equals(u.getEstado())).count();

        long totalReclamos = allClaims.size();
        long reclamosPendientes = allClaims.stream().filter(r -> "PENDIENTE".equals(r.getEstado())).count();
        long reclamosEnRevision = allClaims.stream().filter(r -> "EN_REVISION".equals(r.getEstado())).count();
        long reclamosResueltos = allClaims.stream().filter(r -> "RESUELTO".equals(r.getEstado())).count();

        model.addAttribute("usuario", supportUser);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInhabilitados", usuariosInhabilitados);
        model.addAttribute("totalReclamos", totalReclamos);
        model.addAttribute("reclamosPendientes", reclamosPendientes);
        model.addAttribute("reclamosEnRevision", reclamosEnRevision);
        model.addAttribute("reclamosResueltos", reclamosResueltos);

        // Latest pending claims
        List<Reclamo> latestPendingClaims = allClaims.stream()
                .filter(r -> "PENDIENTE".equals(r.getEstado()))
                .limit(5)
                .toList();
        model.addAttribute("ultimosReclamos", latestPendingClaims);

        return "support/dashboard";
    }

    // ==================== USER MANAGEMENT ======================================

    @GetMapping("/usuarios")
    public String listUsers(
            Authentication authentication,
            Model model,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "buscar", required = false) String buscar) {

        Usuario supportUser = getSupportUser(authentication);
        List<Usuario> usuarios = usuarioService.findAll();

        // Filter only users (not support)
        usuarios = usuarios.stream()
                .filter(u -> "USER".equals(u.getRol()))
                .toList();

        // Filter by status if specified
        if (estado != null && !estado.isEmpty()) {
            usuarios = usuarios.stream()
                    .filter(u -> estado.equals(u.getEstado()))
                    .toList();
        }

        // Filter by search (name or email)
        if (buscar != null && !buscar.trim().isEmpty()) {
            String searchTerm = buscar.toLowerCase().trim();
            usuarios = usuarios.stream()
                    .filter(u -> u.getNombreUsuario().toLowerCase().contains(searchTerm) ||
                                 u.getCorreo().toLowerCase().contains(searchTerm))
                    .toList();
        }

        model.addAttribute("usuario", supportUser);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());

        return "support/usuarios";
    }

    @PostMapping("/usuarios/{id}/cambiar-estado")
    public String changeUserStatus(
            @PathVariable("id") Integer usuarioId,
            @RequestParam("estado") String nuevoEstado,
            Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        try {
            Usuario usuarioActualizar = usuarioService.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Validate it's not a support user
            if ("SUPPORT".equals(usuarioActualizar.getRol())) {
                redirectAttributes.addFlashAttribute("error", "No puedes modificar usuarios de soporte");
                return "redirect:/support/usuarios";
            }

            // Change status
            usuarioActualizar.setEstado(nuevoEstado);
            usuarioService.update(usuarioId, usuarioActualizar);

            redirectAttributes.addFlashAttribute("exito", "Estado del usuario actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }

        return "redirect:/support/usuarios";
    }

    // ==================== CLAIMS MANAGEMENT ======================================

    @GetMapping("/reclamos")
    public String listClaims(
            Authentication authentication,
            Model model,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "categoria", required = false) String categoria) {

        Usuario supportUser = getSupportUser(authentication);
        List<Reclamo> reclamos = reclamoService.findAll();

        // Filter by status if specified
        if (estado != null && !estado.isEmpty()) {
            reclamos = reclamos.stream()
                    .filter(r -> estado.equals(r.getEstado()))
                    .toList();
        }

        // Filter by category if specified
        if (categoria != null && !categoria.isEmpty()) {
            reclamos = reclamos.stream()
                    .filter(r -> categoria.equals(r.getCategoria()))
                    .toList();
        }

        // Statistics
        long pendientes = reclamos.stream().filter(r -> "PENDIENTE".equals(r.getEstado())).count();
        long enRevision = reclamos.stream().filter(r -> "EN_REVISION".equals(r.getEstado())).count();
        long resueltos = reclamos.stream().filter(r -> "RESUELTO".equals(r.getEstado())).count();

        model.addAttribute("usuario", supportUser);
        model.addAttribute("reclamos", reclamos);
        model.addAttribute("totalReclamos", reclamos.size());
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("enRevision", enRevision);
        model.addAttribute("resueltos", resueltos);

        return "support/reclamos";
    }

    @GetMapping("/reclamos/{id}")
    public String viewClaim(
            @PathVariable("id") Integer reclamoId,
            Authentication authentication,
            Model model) {

        Usuario supportUser = getSupportUser(authentication);

        Reclamo reclamo = reclamoService.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

        model.addAttribute("usuario", supportUser);
        model.addAttribute("reclamo", reclamo);

        return "support/reclamo-detalle";
    }

    @PostMapping("/reclamos/{id}/responder")
    public String respondToClaim(
            @PathVariable("id") Integer reclamoId,
            @RequestParam("respuesta") String respuesta,
            @RequestParam("nuevoEstado") String nuevoEstado,
            Authentication authentication,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        try {
            Reclamo reclamo = reclamoService.findById(reclamoId)
                    .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

            // Update response and status
            reclamo.setRespuestaAdmin(respuesta);
            reclamo.setEstado(nuevoEstado);

            if ("RESUELTO".equals(nuevoEstado)) {
                reclamo.setFechaResolucion(LocalDateTime.now());
            }

            reclamoService.update(reclamoId, reclamo);

            redirectAttributes.addFlashAttribute("exito", "Reclamo actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al responder reclamo: " + e.getMessage());
        }

        return "redirect:/support/reclamos";
    }
}
