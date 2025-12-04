package com.example.app.controller.web;

import com.example.app.model.Usuario;
import com.example.app.service.interfaces.ReclamoService;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reclamos")
public class ReclamoWebController {

    @Autowired
    private ReclamoService reclamoService;

    @Autowired
    private UsuarioService usuarioService;

    // Categorías disponibles para reclamos
    private final List<String> CATEGORIAS = Arrays.asList(
            "PROBLEMA_TECNICO", "PROBLEMA_PAGO", "CONSULTA_GENERAL", "SUGERENCIA", "RECLAMO_APUESTA"
    );

    @GetMapping
    public String listarReclamos(
            Authentication authentication,
            Model model,
            @RequestParam(value = "estado", required = false) String estado) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        List<com.example.app.model.Reclamo> reclamos;

        // Aplicar filtro por estado si se especifica
        if (estado != null && !estado.isEmpty()) {
            reclamos = reclamoService.findByUsuarioIdAndEstado(usuario.getId(), estado);
        } else {
            reclamos = reclamoService.findByUsuarioId(usuario.getId());
        }

        // Estadísticas
        long totalReclamos = reclamos.size();
        long reclamosPendientes = reclamos.stream().filter(r -> "PENDIENTE".equals(r.getEstado())).count();
        long reclamosResueltos = reclamos.stream().filter(r -> "RESUELTO".equals(r.getEstado())).count();
        long reclamosRevision = reclamos.stream().filter(r -> "EN_REVISION".equals(r.getEstado())).count();

        model.addAttribute("usuario", usuario);
        model.addAttribute("reclamos", reclamos);
        model.addAttribute("totalReclamos", totalReclamos);
        model.addAttribute("reclamosPendientes", reclamosPendientes);
        model.addAttribute("reclamosResueltos", reclamosResueltos);
        model.addAttribute("reclamosRevision", reclamosRevision);
        model.addAttribute("categorias", CATEGORIAS);

        return "reclamos";
    }

    @PostMapping
    public String crearReclamo(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        try {
            reclamoService.crearReclamo(usuario.getId(), titulo, descripcion, categoria);
            model.addAttribute("exito", "Reclamo creado correctamente. Te contactaremos pronto.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recargar la lista de reclamos
        return listarReclamos(authentication, model, null);
    }

    @GetMapping("/{id}")
    public String verReclamo(
            @PathVariable("id") Integer reclamoId,
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        com.example.app.model.Reclamo reclamo = reclamoService.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

        // Verificar que el reclamo pertenezca al usuario
        if (!reclamo.getUsuario().getId().equals(usuario.getId())) {
            model.addAttribute("error", "No tienes permisos para ver este reclamo");
            return listarReclamos(authentication, model, null);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("reclamo", reclamo);

        return "detalle-reclamo"; // Opcional: página de detalle
    }

    /**
     * Editar un reclamo existente (solo si está en estado PENDIENTE)
     */
    @PostMapping("/{id}/editar")
    public String editarReclamo(
            @PathVariable("id") Integer reclamoId,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        try {
            com.example.app.model.Reclamo reclamo = reclamoService.findById(reclamoId)
                    .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

            // Verificar que el reclamo pertenezca al usuario
            if (!reclamo.getUsuario().getId().equals(usuario.getId())) {
                model.addAttribute("error", "No tienes permisos para editar este reclamo");
                return listarReclamos(authentication, model, null);
            }

            // Solo permitir edición si está en estado PENDIENTE
            if (!"PENDIENTE".equals(reclamo.getEstado())) {
                model.addAttribute("error", "Solo puedes editar reclamos en estado PENDIENTE");
                return listarReclamos(authentication, model, null);
            }

            // Actualizar los campos
            reclamo.setTitulo(titulo);
            reclamo.setDescripcion(descripcion);
            reclamo.setCategoria(categoria);
            reclamoService.save(reclamo);

            model.addAttribute("exito", "Reclamo actualizado correctamente");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error" + e.getMessage());
        }

        return listarReclamos(authentication, model, null);
    }

    /**
     * Eliminar un reclamo (solo si está en estado PENDIENTE)
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarReclamo(
            @PathVariable("id") Integer reclamoId,
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioService.getUsuarioAutenticado(authentication);

        try {
            com.example.app.model.Reclamo reclamo = reclamoService.findById(reclamoId)
                    .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

            // Verificar que el reclamo pertenezca al usuario
            if (!reclamo.getUsuario().getId().equals(usuario.getId())) {
                model.addAttribute("error", "No tienes permisos para eliminar este reclamo");
                return listarReclamos(authentication, model, null);
            }

            // Solo permitir eliminación si está en estado PENDIENTE
            if (!"PENDIENTE".equals(reclamo.getEstado())) {
                model.addAttribute("error", "Solo puedes eliminar reclamos en estado PENDIENTE");
                return listarReclamos(authentication, model, null);
            }

            reclamoService.delete(reclamoId);
            model.addAttribute("exito", "Reclamo eliminado correctamente");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error" + e.getMessage());
        }

        return listarReclamos(authentication, model, null);
    }
}
