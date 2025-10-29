package com.example.app.repository;

import com.example.app.model.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApuestaRepository extends JpaRepository<Apuesta, Integer> {

    // Buscar apuestas por usuario
    List<Apuesta> findByUsuarioId(Integer usuarioId);

    // Buscar apuestas por evento
    List<Apuesta> findByEventoId(Integer eventoId);

    // Buscar apuestas por estado
    List<Apuesta> findByEstado(String estado);

    // Buscar apuestas por usuario y estado
    List<Apuesta> findByUsuarioIdAndEstado(Integer usuarioId, String estado);

    default List<Apuesta> findApuestasPendientesByUsuario(Integer usuarioId) {
        return findByUsuarioIdAndEstado(usuarioId, "PENDIENTE");
    }

    // Buscar apuestas pendientes por evento
    List<Apuesta> findByEventoIdAndEstado(Integer eventoId, String estado);

    // Contar apuestas por usuario
    long countByUsuarioId(Integer usuarioId);

}
