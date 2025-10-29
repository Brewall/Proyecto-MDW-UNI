package com.example.app.service.impl;

import com.example.app.model.Usuario;
import com.example.app.repository.UsuarioRepository;
import com.example.app.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Validaciones básicas
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Verificar que el correo no exista
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Integer id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    existing.setNombreUsuario(usuario.getNombreUsuario());
                    existing.setCorreo(usuario.getCorreo());
                    // No actualizamos password aquí (debería ser endpoint separado)
                    existing.setEstado(usuario.getEstado());
                    return usuarioRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public void delete(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    public List<Usuario> findByEstado(String estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    public Usuario depositarSaldo(Integer usuarioId, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setSaldo(usuario.getSaldo() + monto);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario retirarSaldo(Integer usuarioId, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (usuario.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        usuario.setSaldo(usuario.getSaldo() - monto);
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean tieneSaldoSuficiente(Integer usuarioId, Double monto) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getSaldo() >= monto)
                .orElse(false);
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario).isPresent();
    }
}
