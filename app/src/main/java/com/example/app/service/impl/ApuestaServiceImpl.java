package com.example.app.service.impl;

import com.example.app.model.*;
import com.example.app.repository.ApuestaRepository;
import com.example.app.repository.CuotaRepository;
import com.example.app.repository.UsuarioRepository;
import com.example.app.service.interfaces.ApuestaService;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApuestaServiceImpl implements ApuestaService {

    @Autowired
    private ApuestaRepository apuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private TransaccionService transaccionService;

    @Override
    public List<Apuesta> findAll() {
        return apuestaRepository.findAll();
    }

    @Override
    public Optional<Apuesta> findById(Integer id) {
        return apuestaRepository.findById(id);
    }

    @Override
    public Apuesta save(Apuesta apuesta) {
        return apuestaRepository.save(apuesta);
    }

    @Override
    public void delete(Integer id) {
        if (!apuestaRepository.existsById(id)) {
            throw new IllegalArgumentException("Apuesta no encontrada");
        }
        apuestaRepository.deleteById(id);
    }

    @Override
    public List<Apuesta> findByUsuarioId(Integer usuarioId) {
        return apuestaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Apuesta> findByUsuarioIdWithEventoAndCuota(Integer usuarioId) {
        return apuestaRepository.findByUsuarioIdWithEventoAndCuota(usuarioId);
    }

    @Override
    public List<Apuesta> findByEventoId(Integer eventoId) {
        return apuestaRepository.findByEventoId(eventoId);
    }

    @Override
    public List<Apuesta> findApuestasPendientesByUsuario(Integer usuarioId) {
        return apuestaRepository.findByUsuarioIdAndEstado(usuarioId, "PENDIENTE");
    }

    @Override
    public Apuesta realizarApuesta(Integer usuarioId, Integer cuotaId, Double monto) {
        // Validar monto mínimo
        if (monto < 1.00) {
            throw new IllegalArgumentException("El monto mínimo de apuesta es $1.00");
        }

        // Validar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        if ("INHABILITADO".equals(usuario.getEstado())) {
            throw new IllegalArgumentException("Tu cuenta está inhabilitada. No puedes realizar apuestas.");
        }
        
        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new IllegalArgumentException("Tu cuenta no está activa. No puedes realizar apuestas.");
        }

        // Validar saldo
        if (usuario.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente. Tu saldo es $" + String.format("%.2f", usuario.getSaldo()));
        }

        // Validar cuota
        Cuota cuota = cuotaRepository.findById(cuotaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada"));
        
        if (!"DISPONIBLE".equals(cuota.getEstado())) {
            throw new IllegalArgumentException("Esta cuota ya no está disponible para apostar");
        }

        // Validar evento
        Evento evento = cuota.getEvento();
        if (!"PROGRAMADO".equals(evento.getEstado())) {
            throw new IllegalArgumentException("Este evento ya no acepta apuestas");
        }
        
        if (evento.getFechaEvento().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Este evento ya ha comenzado o finalizado");
        }

        // Descontar saldo
        usuario.setSaldo(usuario.getSaldo() - monto);
        usuarioRepository.save(usuario);

        // Crear apuesta
        Apuesta apuesta = new Apuesta(usuario, cuota.getEvento(), cuota, monto);
        Apuesta apuestaGuardada = apuestaRepository.save(apuesta);

        // Registrar transacción (incluir ID de apuesta para poder correlacionar con cancelaciones)
        transaccionService.registrarApuesta(usuarioId, monto,
                "Apuesta en " + cuota.getEvento().getNombreEvento() + " #" + apuestaGuardada.getId());

        return apuestaGuardada;
    }

    @Override
    public void procesarApuestaGanada(Integer apuestaId) {
        Apuesta apuesta = apuestaRepository.findById(apuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Apuesta no encontrada"));

        apuesta.setEstado("GANADA");
        apuestaRepository.save(apuesta);

        // Pagar ganancia al usuario
        Double ganancia = apuesta.getGananciaPotencial();
        Usuario usuario = apuesta.getUsuario();
        usuario.setSaldo(usuario.getSaldo() + ganancia);
        usuarioRepository.save(usuario);

        // Registrar transacción de ganancia
        transaccionService.registrarGanancia(usuario.getId(), ganancia,
                "Ganancia apuesta #" + apuestaId);
    }

    @Override
    public void procesarApuestaPerdida(Integer apuestaId) {
        Apuesta apuesta = apuestaRepository.findById(apuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Apuesta no encontrada"));

        apuesta.setEstado("PERDIDA");
        apuestaRepository.save(apuesta);
        // No se devuelve el dinero (ya fue descontado al hacer la apuesta)
    }

    @Override
    public void cancelarApuesta(Integer apuestaId) {
        Apuesta apuesta = apuestaRepository.findById(apuestaId)
                .orElseThrow(() -> new IllegalArgumentException("Apuesta no encontrada"));

        if (!"PENDIENTE".equals(apuesta.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden cancelar apuestas pendientes");
        }

        // Devolver dinero al usuario
        Usuario usuario = apuesta.getUsuario();
        usuario.setSaldo(usuario.getSaldo() + apuesta.getMonto());
        usuarioRepository.save(usuario);

        apuesta.setEstado("CANCELADA");
        apuestaRepository.save(apuesta);

        // Registrar transacción de devolución
        transaccionService.registrarDeposito(usuario.getId(), apuesta.getMonto(),
                "Devolución apuesta cancelada #" + apuestaId);
    }

    @Override
    public List<Apuesta> findByUsuarioIdAndEstado(Integer id, String estado) {
        return List.of();
    }
}
