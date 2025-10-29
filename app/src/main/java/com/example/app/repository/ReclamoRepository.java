package com.example.app.repository;

import com.example.app.model.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {

    // Buscar reclamos por usuario
    List<Reclamo> findByUsuarioId(Integer usuarioId);

    // Buscar reclamos por estado
    List<Reclamo> findByEstado(String estado);

    // Buscar reclamos por usuario y estado
    List<Reclamo> findByUsuarioIdAndEstado(Integer usuarioId, String estado);

    // Buscar reclamos por categoría
    List<Reclamo> findByCategoria(String categoria);

    // Contar reclamos pendientes por usuario
    long countByUsuarioIdAndEstado(Integer usuarioId, String estado);
}
