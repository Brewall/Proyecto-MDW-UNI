package com.example.app.repository;

import com.example.app.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    // Buscar eventos por estado
    List<Evento> findByEstado(String estado);

    default List<Evento> findEventosProgramados() {
        return findByEstado("PROGRAMADO");
    }

    default List<Evento> findEventosFinalizados() {
        return findByEstado("FINALIZADO");
    }

    // Buscar eventos por deporte
    List<Evento> findByDeporte(String deporte);

    // Buscar eventos programados después de una fecha
    List<Evento> findByFechaEventoAfter(LocalDateTime fecha);

    // Buscar eventos por deporte y estado
    List<Evento> findByDeporteAndEstado(String deporte, String estado);

    // Buscar eventos pendientes ordenados por fecha
    List<Evento> findByEstadoOrderByFechaEventoAsc(String estado);
}
