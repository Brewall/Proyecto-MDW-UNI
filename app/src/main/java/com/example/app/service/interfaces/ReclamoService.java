package com.example.app.service.interfaces;

import com.example.app.model.Reclamo;
import java.util.List;
import java.util.Optional;

public interface ReclamoService {

    // CRUD básico
    List<Reclamo> findAll();
    Optional<Reclamo> findById(Integer id);
    Reclamo save(Reclamo reclamo);
    Reclamo update(Integer id, Reclamo reclamo);
    void delete(Integer id);

    // Búsquedas específicas
    List<Reclamo> findByUsuarioId(Integer usuarioId);
    List<Reclamo> findByEstado(String estado);
    List<Reclamo> findByUsuarioIdAndEstado(Integer usuarioId, String estado);

    // Operaciones de negocio
    Reclamo crearReclamo(Integer usuarioId, String titulo, String descripcion, String categoria);
    Reclamo marcarComoResuelto(Integer reclamoId, String respuestaAdmin);
    Reclamo marcarComoEnRevision(Integer reclamoId);

    // Estadísticas
    long countReclamosPendientesUsuario(Integer usuarioId);
    long countByEstado(String estado);
}
