package com.example.app.service.interfaces;

import com.example.app.model.Cuota;
import java.util.List;
import java.util.Optional;

public interface CuotaService {

    // CRUD básico
    List<Cuota> findAll();
    Optional<Cuota> findById(Integer id);
    Cuota save(Cuota cuota);
    Cuota update(Integer id, Cuota cuota);
    void delete(Integer id);

    // Búsquedas específicas
    List<Cuota> findByEventoId(Integer eventoId);
    List<Cuota> findCuotasDisponiblesByEvento(Integer eventoId);
    boolean existeCuotaParaEvento(Integer eventoId, String descripcion);

    // Gestión de estados
    Cuota cerrarCuota(Integer cuotaId);
    Cuota marcarComoGanadora(Integer cuotaId);
    Cuota marcarComoPerdedora(Integer cuotaId);

    // Validaciones
    boolean cuotaEstaDisponible(Integer cuotaId);
}
