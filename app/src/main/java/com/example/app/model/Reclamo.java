package com.example.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamo")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 20)
    private String estado = "PENDIENTE"; // PENDIENTE, EN_REVISION, RESUELTO

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "respuesta_admin", columnDefinition = "TEXT")
    private String respuestaAdmin;

    @Column(length = 50)
    private String categoria; // PROBLEMA_TECNICO, PROBLEMA_PAGO, CONSULTA, SUGERENCIA

    // --- CONSTRUCTORES ---
    public Reclamo() {}

    public Reclamo(Usuario usuario, String titulo, String descripcion, String categoria) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public String getRespuestaAdmin() { return respuestaAdmin; }
    public void setRespuestaAdmin(String respuestaAdmin) { this.respuestaAdmin = respuestaAdmin; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // --- MÉTODOS UTILES ---
    public boolean isResuelto() {
        return "RESUELTO".equals(estado);
    }

    public boolean isPendiente() {
        return "PENDIENTE".equals(estado);
    }

    @Override
    public String toString() {
        return "Reclamo{id=" + id + ", titulo='" + titulo + "', estado='" + estado + "', categoria='" + categoria + "'}";
    }
}
