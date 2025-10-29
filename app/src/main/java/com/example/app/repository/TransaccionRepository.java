package com.example.app.repository;

import com.example.app.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    // Buscar transacciones por usuario
    List<Transaccion> findByUsuarioId(Integer usuarioId);

    // Buscar transacciones por tipo
    List<Transaccion> findByTipo(String tipo);

    // Buscar transacciones por usuario y tipo
    List<Transaccion> findByUsuarioIdAndTipo(Integer usuarioId, String tipo);

    // Buscar transacciones por rango de fechas
    List<Transaccion> findByFechaTransaccionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar transacciones de un usuario ordenadas por fecha (más reciente primero)
    List<Transaccion> findByUsuarioIdOrderByFechaTransaccionDesc(Integer usuarioId);

    // Calcular balance total de transacciones de un usuario
    @Query("SELECT COALESCE(SUM(t.monto), 0) FROM Transaccion t WHERE t.usuario.id = :usuarioId")
    Double calcularBalanceUsuario(Integer usuarioId);
}