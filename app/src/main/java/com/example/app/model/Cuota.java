package com.example.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cuota")
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(nullable = false, length = 50)
    private String descripcion;

    @Column(nullable = false)
    private Double valor;

    @Column(length = 15)
    private String estado = "DISPONIBLE";

    // --- CONSTRUCTORES ---
    public Cuota() {}

    public Cuota(Evento evento, String descripcion, Double valor) {
        this.evento = evento;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Cuota{id=" + id + ", descripcion='" + descripcion + "', valor=" + valor + ", estado='" + estado + "'}";
    }
}