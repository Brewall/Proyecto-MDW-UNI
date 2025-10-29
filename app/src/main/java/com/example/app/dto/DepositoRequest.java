package com.example.app.dto;

public class DepositoRequest {
    private Integer usuarioId;
    private Double monto;
    private String descripcion;

    // --- CONSTRUCTORES ---
    public DepositoRequest() {}

    public DepositoRequest(Integer usuarioId, Double monto, String descripcion) {
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
