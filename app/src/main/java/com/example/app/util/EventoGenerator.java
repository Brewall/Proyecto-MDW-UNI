package com.example.app.util;

import com.example.app.model.Cuota;
import com.example.app.model.Evento;
import com.example.app.service.interfaces.EventoService;
import com.example.app.service.interfaces.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class EventoGenerator {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private CuotaService cuotaService;

    private Random random = new Random();

    /**
     * Genera eventos automáticos cada hora (para testing)
     */
    @Scheduled(fixedRate = 3600000) // Cada hora
    public void generarEventosAutomaticos() {
        if (random.nextDouble() < 0.3) { // 30% de probabilidad
            String deporte = AppConstants.DEPORTES[random.nextInt(AppConstants.DEPORTES.length)];
            String equipo1 = generarNombreEquipo();
            String equipo2 = generarNombreEquipo();

            while (equipo1.equals(equipo2)) {
                equipo2 = generarNombreEquipo();
            }

            Evento evento = new Evento(
                    equipo1 + " vs " + equipo2,
                    deporte,
                    LocalDateTime.now().plusHours(random.nextInt(24) + 1) // 1-24 horas en el futuro
            );

            evento.setDescripcion("Partido de " + deporte + " - " + evento.getNombreEvento());

            Evento eventoCreado = eventoService.save(evento);

            // Crear cuotas para el evento
            crearCuotasParaEvento(eventoCreado);
        }
    }

    private void crearCuotasParaEvento(Evento evento) {
        String[] descripciones = {"Local", "Empate", "Visitante"};
        Double[] valores = {2.10, 3.50, 2.80}; // Valores de ejemplo

        for (int i = 0; i < descripciones.length; i++) {
            Cuota cuota = new Cuota(evento, descripciones[i], valores[i]);
            cuotaService.save(cuota);
        }
    }

    private String generarNombreEquipo() {
        String[] equipos = {
                "Barcelona", "Real Madrid", "Manchester", "Liverpool", "Bayern",
                "PSG", "Juventus", "Milan", "Ajax", "Dortmund", "Chelsea", "Arsenal"
        };
        return equipos[random.nextInt(equipos.length)];
    }
}