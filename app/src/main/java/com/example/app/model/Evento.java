package com.example.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_evento", nullable = false, length = 100)
    private String nombreEvento;

    @Column(nullable = false, length = 30)
    private String deporte;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(length = 15)
    private String estado = "PROGRAMADO";

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuota> cuotas = new ArrayList<>();

    // --- CONSTRUCTORES ---
    public Evento() {}

    public Evento(String nombreEvento, String deporte, LocalDateTime fechaEvento) {
        this.nombreEvento = nombreEvento;
        this.deporte = deporte;
        this.fechaEvento = fechaEvento;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }

    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDateTime fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Cuota> getCuotas() { return cuotas; }
    public void setCuotas(List<Cuota> cuotas) { this.cuotas = cuotas; }

    @Override
    public String toString() {
        return "Evento{id=" + id + ", nombreEvento='" + nombreEvento + "', deporte='" + deporte + "', estado='" + estado + "'}";
    }
}