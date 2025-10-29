package com.example.app.repository;

import com.example.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por correo (para login)
    Optional<Usuario> findByCorreo(String correo);

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Verificar si existe un usuario con ese correo
    boolean existsByCorreo(String correo);

    // Buscar usuarios por estado
    List<Usuario> findByEstado(String estado);
}
