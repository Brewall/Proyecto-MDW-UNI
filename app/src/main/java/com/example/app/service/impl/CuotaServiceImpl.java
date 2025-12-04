package com.example.app.service.impl;

import com.example.app.model.Cuota;
import com.example.app.repository.CuotaRepository;
import com.example.app.service.interfaces.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CuotaServiceImpl implements CuotaService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Override
    public List<Cuota> findAll() {
        return cuotaRepository.findAll();
    }

    @Override
    public Optional<Cuota> findById(Integer id) {
        return cuotaRepository.findById(id);
    }

    @Override
    public Cuota save(Cuota cuota) {
        // Validar valor de cuota
        if (cuota.getValor() < 1.10) {
            throw new IllegalArgumentException("El valor de la cuota debe ser al menos 1.10");
        }

        // Validar que no exista misma descripción para el evento
        if (cuotaRepository.existsByEventoIdAndDescripcion(
                cuota.getEvento().getId(), cuota.getDescripcion())) {
            throw new IllegalArgumentException("Ya existe una cuota con esa descripción para este evento");
        }

        return cuotaRepository.save(cuota);
    }

    @Override
    public Cuota update(Integer id, Cuota cuota) {
        return cuotaRepository.findById(id)
                .map(existing -> {
                    existing.setDescripcion(cuota.getDescripcion());
                    existing.setValor(cuota.getValor());
                    existing.setEstado(cuota.getEstado());
                    return cuotaRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada"));
    }

    @Override
    public void delete(Integer id) {
        if (!cuotaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cuota no encontrada");
        }
        cuotaRepository.deleteById(id);
    }

    @Override
    public List<Cuota> findByEventoId(Integer eventoId) {
        return cuotaRepository.findByEventoId(eventoId);
    }

    @Override
    public List<Cuota> findCuotasDisponiblesByEvento(Integer eventoId) {
        return cuotaRepository.findCuotasDisponiblesByEvento(eventoId);
    }
}
