package com.example.app.service.impl;

import com.example.app.model.Transaccion;
import com.example.app.model.Usuario;
import com.example.app.repository.TransaccionRepository;
import com.example.app.repository.UsuarioRepository;
import com.example.app.service.interfaces.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Transaccion> findAll() {
        return transaccionRepository.findAll();
    }

    @Override
    public Optional<Transaccion> findById(Integer id) {
        return transaccionRepository.findById(id);
    }

    @Override
    public Transaccion save(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    @Override
    public List<Transaccion> findByUsuarioId(Integer usuarioId) {
        return transaccionRepository.findByUsuarioIdOrderByFechaTransaccionDesc(usuarioId);
    }

    @Override
    public List<Transaccion> findByTipo(String tipo) {
        return transaccionRepository.findByTipo(tipo);
    }

    @Override
    public Transaccion registrarDeposito(Integer usuarioId, Double monto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Transaccion transaccion = Transaccion.crearDeposito(usuario, monto, descripcion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion registrarApuesta(Integer usuarioId, Double monto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Transaccion transaccion = Transaccion.crearApuesta(usuario, monto, descripcion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion registrarGanancia(Integer usuarioId, Double monto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Transaccion transaccion = Transaccion.crearGanancia(usuario, monto, descripcion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion registrarRetiro(Integer usuarioId, Double monto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar saldo suficiente
        if (usuario.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente para retiro");
        }

        Transaccion transaccion = Transaccion.crearRetiro(usuario, monto, descripcion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Double calcularGananciasTotalesUsuario(Integer usuarioId) {
        List<Transaccion> ganancias = transaccionRepository.findByUsuarioIdAndTipo(usuarioId, "GANANCIA");
        return ganancias.stream()
                .mapToDouble(Transaccion::getMonto)
                .sum();
    }

    @Override
    public List<Transaccion> findByUsuarioIdAndTipo(Integer id, String tipo) {
        return List.of();
    }
}
