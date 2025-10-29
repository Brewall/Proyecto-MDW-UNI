package com.example.app.repository;

import com.example.app.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Integer> {

    // Buscar todas las cuotas de un evento
    List<Cuota> findByEventoId(Integer eventoId);

    // Buscar cuotas por evento y estado específico
    List<Cuota> findByEventoIdAndEstado(Integer eventoId, String estado);

    // MÉTODOS DEFAULT - SEMÁNTICOS Y LEGIBLES
    default List<Cuota> findCuotasDisponiblesByEvento(Integer eventoId) {
        return findByEventoIdAndEstado(eventoId, "DISPONIBLE");
    }

    default List<Cuota> findCuotasCerradasByEvento(Integer eventoId) {
        return findByEventoIdAndEstado(eventoId, "CERRADA");
    }

    default List<Cuota> findCuotasGanadorasByEvento(Integer eventoId) {
        return findByEventoIdAndEstado(eventoId, "GANADORA");
    }

    default List<Cuota> findCuotasPerdedorasByEvento(Integer eventoId) {
        return findByEventoIdAndEstado(eventoId, "PERDEDORA");
    }

    // Verifica si existe una cuota para un evento específico
    boolean existsByEventoIdAndDescripcion(Integer eventoId, String descripcion);

    // Buscar cuotas por estado (sin filtrar por evento)
    List<Cuota> findByEstado(String estado);
}