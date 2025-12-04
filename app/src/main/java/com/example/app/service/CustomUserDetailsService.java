package com.example.app.service;

import com.example.app.model.Usuario;
import com.example.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio que conecta Spring Security con nuestra tabla de usuarios.
 * Spring Security usa este servicio para cargar los datos del usuario durante el login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su "username" (en nuestro caso, el correo).
     * Este método es llamado automáticamente por Spring Security durante el login.
     * 
     * NOTA: Usuarios INHABILITADOS pueden hacer login (solo tienen restricción para apostar).
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // INHABILITADO users CAN login - they just can't place bets
        // Only truly disabled/deleted accounts should be blocked here if needed in the future

        // Determinar el rol del usuario (por defecto USER si es null)
        String rol = usuario.getRol() != null ? usuario.getRol() : "USER";

        // Construir el objeto UserDetails que Spring Security necesita
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())  // La contraseña ya viene hasheada de la BD
                .roles(rol)                       // Dynamic role: USER or SUPPORT
                .build();
    }
}
