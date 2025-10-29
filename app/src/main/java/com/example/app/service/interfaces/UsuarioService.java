package com.example.app.service.interfaces;

import com.example.app.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // CRUD básico
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    Usuario update(Integer id, Usuario usuario);
    void delete(Integer id);

    // Búsquedas específicas
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    List<Usuario> findByEstado(String estado);

    // Gestión de saldos
    Usuario depositarSaldo(Integer usuarioId, Double monto);
    Usuario retirarSaldo(Integer usuarioId, Double monto);
    boolean tieneSaldoSuficiente(Integer usuarioId, Double monto);

    // Validaciones
    boolean existeCorreo(String correo);
    boolean existeNombreUsuario(String nombreUsuario);
}
