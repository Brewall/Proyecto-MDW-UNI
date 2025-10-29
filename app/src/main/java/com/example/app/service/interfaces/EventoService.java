package com.example.app.service.interfaces;

import com.example.app.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {

    // CRUD básico
    List<Evento> findAll();
    Optional<Evento> findById(Integer id);
    Evento save(Evento evento);
    Evento update(Integer id, Evento evento);
    void delete(Integer id);

    // Búsquedas específicas
    List<Evento> findByEstado(String estado);
    List<Evento> findByDeporte(String deporte);
    List<Evento> findEventosProgramados();
    List<Evento> findEventosDisponiblesParaApuestas();

    // Gestión de estados
    Evento iniciarEvento(Integer eventoId);
    Evento finalizarEvento(Integer eventoId);
    Evento cancelarEvento(Integer eventoId);

    // Validaciones
    boolean eventoPuedeApostarse(Integer eventoId);
}
