package com.example.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 10)
    private String tipo; // DEPOSITO, RETIRO, APUESTA, GANANCIA

    @Column(nullable = false)
    private Double monto; // Positivo: deposito/ganancia, Negativo: apuesta/retiro

    @Column(length = 100)
    private String descripcion;

    @Column(name = "fecha_transaccion")
    private LocalDateTime fechaTransaccion = LocalDateTime.now();

    // --- CONSTRUCTORES ---
    public Transaccion() {}

    public Transaccion(Usuario usuario, String tipo, Double monto, String descripcion) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    // --- CONSTRUCTORES ESPECÍFICOS ---
    public static Transaccion crearDeposito(Usuario usuario, Double monto, String descripcion) {
        return new Transaccion(usuario, "DEPOSITO", Math.abs(monto), descripcion);
    }

    public static Transaccion crearApuesta(Usuario usuario, Double monto, String descripcion) {
        return new Transaccion(usuario, "APUESTA", -Math.abs(monto), descripcion);
    }

    public static Transaccion crearGanancia(Usuario usuario, Double monto, String descripcion) {
        return new Transaccion(usuario, "GANANCIA", Math.abs(monto), descripcion);
    }

    public static Transaccion crearRetiro(Usuario usuario, Double monto, String descripcion) {
        return new Transaccion(usuario, "RETIRO", -Math.abs(monto), descripcion);
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    // --- MÉTODO UTILITARIO ---
    public boolean esPositiva() {
        return monto >= 0;
    }

    @Override
    public String toString() {
        return "Transaccion{id=" + id + ", tipo='" + tipo + "', monto=" + monto +
                ", descripcion='" + descripcion + "', fecha=" + fechaTransaccion + "}";
    }
}