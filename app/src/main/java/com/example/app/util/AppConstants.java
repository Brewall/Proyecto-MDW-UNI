package com.example.app.util;

public class AppConstants {

    // Estados de Usuario
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_SUSPENDIDO = "SUSPENDIDO";

    // Estados de Evento
    public static final String ESTADO_PROGRAMADO = "PROGRAMADO";
    public static final String ESTADO_EN_CURSO = "EN_CURSO";
    public static final String ESTADO_FINALIZADO = "FINALIZADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    // Estados de Apuesta
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_GANADA = "GANADA";
    public static final String ESTADO_PERDIDA = "PERDIDA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    // Estados de Cuota
    public static final String ESTADO_DISPONIBLE = "DISPONIBLE";
    public static final String ESTADO_CERRADA = "CERRADA";
    public static final String ESTADO_GANADORA = "GANADORA";
    public static final String ESTADO_PERDEDORA = "PERDEDORA";

    // Tipos de Transacción
    public static final String TIPO_DEPOSITO = "DEPOSITO";
    public static final String TIPO_RETIRO = "RETIRO";
    public static final String TIPO_APUESTA = "APUESTA";
    public static final String TIPO_GANANCIA = "GANANCIA";

    // Deportes disponibles
    public static final String[] DEPORTES = {
            "Fútbol", "Baloncesto", "Tenis", "Vóley", "Béisbol", "Boxeo", "eSports"
    };

    // Configuraciones
    public static final Double MONTO_MINIMO_APUESTA = 1.00;
    public static final Double MONTO_MAXIMO_APUESTA = 1000.00;
    public static final Double SALDO_INICIAL = 100.00;
}
