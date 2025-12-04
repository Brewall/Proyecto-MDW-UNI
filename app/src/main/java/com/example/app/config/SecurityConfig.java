package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración del encoder de contraseñas.
     * BCrypt es el estándar de la industria para hash de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Handler personalizado para redirigir según el rol después del login.
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            boolean isSupport = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPPORT"));
            
            if (isSupport) {
                response.sendRedirect("/support/dashboard");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    /**
     * Configuración principal de seguridad.
     * Define qué rutas son públicas y cuáles requieren autenticación.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización de rutas
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (accesibles sin login)
                .requestMatchers(
                    "/login",
                    "/registro",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "/error"
                ).permitAll()
                
                // Support-only routes (admin panel)
                .requestMatchers("/support/**").hasRole("SUPPORT")
                
                // User-only routes (betting, deposits, etc.)
                .requestMatchers("/apuestas/**").hasRole("USER")
                .requestMatchers("/perfil/depositar", "/perfil/retirar").hasRole("USER")
                
                // Rutas compartidas (ambos roles pueden acceder)
                .requestMatchers("/dashboard", "/perfil", "/transacciones", "/reclamos").authenticated()
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            // Configuración del formulario de login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("correo")
                .passwordParameter("password")
                .successHandler(successHandler())       // Redirigir según rol
                .failureUrl("/login?error=true")
                .permitAll()
            )
            // Configuración de logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Configuración de "recuérdame"
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(86400)
            );

        return http.build();
    }
}
