package com.example.app.config;

import com.example.app.model.Usuario;
import com.example.app.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializer that creates the default SUPPORT user if it doesn't exist.
 * This user has administrative privileges to manage users and claims.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if support user exists
        if (!usuarioRepository.existsByCorreo("soporte@casino.com")) {
            Usuario soporte = new Usuario();
            soporte.setNombreUsuario("Soporte Admin");
            soporte.setCorreo("soporte@casino.com");
            soporte.setPassword(passwordEncoder.encode("soporte123")); // Change in production
            soporte.setRol("SUPPORT");  // Role in English for code consistency
            soporte.setEstado("ACTIVO");
            soporte.setSaldo(0.0);
            
            usuarioRepository.save(soporte);
            logger.info("SUPPORT user created: soporte@casino.com / soporte123");
        } else {
            logger.info("SUPPORT user already exists");
        }
    }
}
