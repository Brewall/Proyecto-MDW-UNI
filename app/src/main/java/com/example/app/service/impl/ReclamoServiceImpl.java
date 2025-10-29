package com.example.app.service.impl;

import com.example.app.model.Reclamo;
import com.example.app.model.Usuario;
import com.example.app.repository.ReclamoRepository;
import com.example.app.repository.UsuarioRepository;
import com.example.app.service.interfaces.ReclamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReclamoServiceImpl implements ReclamoService {

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Reclamo> findAll() {
        return reclamoRepository.findAll();
    }

    @Override
    public Optional<Reclamo> findById(Integer id) {
        return reclamoRepository.findById(id);
    }

    @Override
    public Reclamo save(Reclamo reclamo) {
        // Validaciones básicas
        if (reclamo.getTitulo() == null || reclamo.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del reclamo es obligatorio");
        }
        if (reclamo.getDescripcion() == null || reclamo.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del reclamo es obligatoria");
        }

        return reclamoRepository.save(reclamo);
    }

    @Override
    public Reclamo update(Integer id, Reclamo reclamo) {
        return reclamoRepository.findById(id)
                .map(existing -> {
                    existing.setTitulo(reclamo.getTitulo());
                    existing.setDescripcion(reclamo.getDescripcion());
                    existing.setCategoria(reclamo.getCategoria());
                    existing.setEstado(reclamo.getEstado());
                    existing.setRespuestaAdmin(reclamo.getRespuestaAdmin());
                    return reclamoRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));
    }

    @Override
    public void delete(Integer id) {
        if (!reclamoRepository.existsById(id)) {
            throw new IllegalArgumentException("Reclamo no encontrado");
        }
        reclamoRepository.deleteById(id);
    }

    @Override
    public List<Reclamo> findByUsuarioId(Integer usuarioId) {
        return reclamoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Reclamo> findByEstado(String estado) {
        return reclamoRepository.findByEstado(estado);
    }

    @Override
    public List<Reclamo> findByUsuarioIdAndEstado(Integer usuarioId, String estado) {
        return reclamoRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }

    @Override
    public Reclamo crearReclamo(Integer usuarioId, String titulo, String descripcion, String categoria) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Reclamo reclamo = new Reclamo(usuario, titulo, descripcion, categoria);
        return reclamoRepository.save(reclamo);
    }

    @Override
    public Reclamo marcarComoResuelto(Integer reclamoId, String respuestaAdmin) {
        Reclamo reclamo = reclamoRepository.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

        reclamo.setEstado("RESUELTO");
        reclamo.setRespuestaAdmin(respuestaAdmin);
        reclamo.setFechaResolucion(LocalDateTime.now());

        return reclamoRepository.save(reclamo);
    }

    @Override
    public Reclamo marcarComoEnRevision(Integer reclamoId) {
        Reclamo reclamo = reclamoRepository.findById(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado"));

        reclamo.setEstado("EN_REVISION");
        reclamo.setRespuestaAdmin(null);
        reclamo.setFechaResolucion(null);

        return reclamoRepository.save(reclamo);
    }

    @Override
    public long countReclamosPendientesUsuario(Integer usuarioId) {
        return reclamoRepository.countByUsuarioIdAndEstado(usuarioId, "PENDIENTE");
    }

    @Override
    public long countByEstado(String estado) {
        return reclamoRepository.findByEstado(estado).size();
    }
}
