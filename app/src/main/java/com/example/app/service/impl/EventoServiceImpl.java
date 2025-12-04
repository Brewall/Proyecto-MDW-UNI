package com.example.app.service.impl;

import com.example.app.model.Evento;
import com.example.app.repository.EventoRepository;
import com.example.app.service.interfaces.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    @Override
    public Optional<Evento> findById(Integer id) {
        return eventoRepository.findById(id);
    }

    @Override
    public Evento save(Evento evento) {
        // Validar fecha del evento
        if (evento.getFechaEvento().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento no puede ser en el pasado");
        }
        return eventoRepository.save(evento);
    }

    @Override
    public Evento update(Integer id, Evento evento) {
        return eventoRepository.findById(id)
                .map(existing -> {
                    existing.setNombreEvento(evento.getNombreEvento());
                    existing.setDeporte(evento.getDeporte());
                    existing.setFechaEvento(evento.getFechaEvento());
                    existing.setDescripcion(evento.getDescripcion());
                    return eventoRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
    }

    @Override
    public void delete(Integer id) {
        if (!eventoRepository.existsById(id)) {
            throw new IllegalArgumentException("Evento no encontrado");
        }
        eventoRepository.deleteById(id);
    }

    @Override
    public List<Evento> findByEstado(String estado) {
        return eventoRepository.findByEstado(estado);
    }

    @Override
    public List<Evento> findByDeporte(String deporte) {
        return eventoRepository.findByDeporte(deporte);
    }

    @Override
    public List<Evento> findEventosProgramados() {
        return eventoRepository.findByEstadoOrderByFechaEventoAsc("PROGRAMADO");
    }

    @Override
    public List<Evento> findEventosDisponiblesParaApuestas() {
        return eventoRepository.findByEstado("PROGRAMADO");
    }
}