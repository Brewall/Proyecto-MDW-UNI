package com.example.app.service.interfaces;

import com.example.app.model.Transaccion;
import java.util.List;
import java.util.Optional;

public interface TransaccionService {

    // CRUD básico
    List<Transaccion> findAll();
    Optional<Transaccion> findById(Integer id);
    Transaccion save(Transaccion transaccion);

    // Búsquedas específicas
    List<Transaccion> findByUsuarioId(Integer usuarioId);
    List<Transaccion> findByTipo(String tipo);
    List<Transaccion> findHistorialUsuario(Integer usuarioId);

    // Operaciones de negocio
    Transaccion registrarDeposito(Integer usuarioId, Double monto, String descripcion);
    Transaccion registrarApuesta(Integer usuarioId, Double monto, String descripcion);
    Transaccion registrarGanancia(Integer usuarioId, Double monto, String descripcion);
    Transaccion registrarRetiro(Integer usuarioId, Double monto, String descripcion);

    // Consultas financieras
    Double calcularBalanceUsuario(Integer usuarioId);
    Double calcularGananciasTotalesUsuario(Integer usuarioId);

    List<Transaccion> findByUsuarioIdAndTipo(Integer id, String tipo);
}
