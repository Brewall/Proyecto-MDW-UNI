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
 * Inicializador que crea el usuario de soporte por defecto.
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
        if (!usuarioRepository.existsByCorreo("soporte@casino.com")) {
            Usuario soporte = new Usuario();
            soporte.setNombreUsuario("Soporte Admin");
            soporte.setCorreo("soporte@casino.com");
            soporte.setPassword(passwordEncoder.encode("soporte123"));
            soporte.setRol("SUPPORT");
            soporte.setEstado("ACTIVO");
            soporte.setSaldo(0.0);
            
            usuarioRepository.save(soporte);
            logger.info("Usuario SUPPORT creado: soporte@casino.com / soporte123");
        } else {
            logger.info("Usuario SUPPORT ya existe");
        }
    }
}
