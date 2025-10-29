package com.example.app.service.interfaces;

import com.example.app.model.Apuesta;
import java.util.List;
import java.util.Optional;

public interface ApuestaService {

    // CRUD básico
    List<Apuesta> findAll();
    Optional<Apuesta> findById(Integer id);
    Apuesta save(Apuesta apuesta);
    void delete(Integer id);

    // Búsquedas específicas
    List<Apuesta> findByUsuarioId(Integer usuarioId);
    List<Apuesta> findByEventoId(Integer eventoId);
    List<Apuesta> findByEstado(String estado);
    List<Apuesta> findApuestasPendientesByUsuario(Integer usuarioId);

    // Operaciones de negocio
    Apuesta realizarApuesta(Integer usuarioId, Integer cuotaId, Double monto);
    void procesarApuestaGanada(Integer apuestaId);
    void procesarApuestaPerdida(Integer apuestaId);
    void cancelarApuesta(Integer apuestaId);

    // Procesamiento por eventos
    void procesarResultadosEvento(Integer eventoId, Integer cuotaGanadoraId);

    // Validaciones
    boolean puedeRealizarApuesta(Integer usuarioId, Integer cuotaId, Double monto);

    List<Apuesta> findByUsuarioIdAndEstado(Integer id, String estado);
}
