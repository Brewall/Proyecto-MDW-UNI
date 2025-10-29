package com.example.app.dto;

public class LoginRequest {
    private String correo;
    private String password;

    // --- CONSTRUCTORES ---
    public LoginRequest() {}

    public LoginRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    // --- GETTERS Y SETTERS ---
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
