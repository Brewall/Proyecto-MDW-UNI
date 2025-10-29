package com.example.app.dto;

public class ApuestaRequest {
    private Integer usuarioId;
    private Integer cuotaId;
    private Double monto;

    // --- CONSTRUCTORES ---
    public ApuestaRequest() {}

    public ApuestaRequest(Integer usuarioId, Integer cuotaId, Double monto) {
        this.usuarioId = usuarioId;
        this.cuotaId = cuotaId;
        this.monto = monto;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getCuotaId() { return cuotaId; }
    public void setCuotaId(Integer cuotaId) { this.cuotaId = cuotaId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
