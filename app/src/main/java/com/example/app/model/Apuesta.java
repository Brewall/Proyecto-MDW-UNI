package com.example.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apuesta")
public class Apuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuota", nullable = false)
    private Cuota cuota;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "fecha_apuesta")
    private LocalDateTime fechaApuesta = LocalDateTime.now();

    @Column(length = 15)
    private String estado = "PENDIENTE";

    @Column(name = "ganancia_potencial")
    private Double gananciaPotencial;

    // --- CONSTRUCTORES ---
    public Apuesta() {}

    public Apuesta(Usuario usuario, Evento evento, Cuota cuota, Double monto) {
        this.usuario = usuario;
        this.evento = evento;
        this.cuota = cuota;
        this.monto = monto;
        this.gananciaPotencial = monto * cuota.getValor();
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Cuota getCuota() { return cuota; }
    public void setCuota(Cuota cuota) { this.cuota = cuota; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) {
        this.monto = monto;
        // Recalcular ganancia potencial si la cuota está disponible
        if (this.cuota != null && this.cuota.getValor() != null) {
            this.gananciaPotencial = monto * this.cuota.getValor();
        }
    }

    public LocalDateTime getFechaApuesta() { return fechaApuesta; }
    public void setFechaApuesta(LocalDateTime fechaApuesta) { this.fechaApuesta = fechaApuesta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getGananciaPotencial() { return gananciaPotencial; }
    public void setGananciaPotencial(Double gananciaPotencial) { this.gananciaPotencial = gananciaPotencial; }

    @Override
    public String toString() {
        return "Apuesta{id=" + id + ", usuario=" + (usuario != null ? usuario.getId() : "null") +
                ", monto=" + monto + ", gananciaPotencial=" + gananciaPotencial + ", estado='" + estado + "'}";
    }
}
