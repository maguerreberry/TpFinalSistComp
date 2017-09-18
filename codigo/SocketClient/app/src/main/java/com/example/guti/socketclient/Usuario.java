package com.example.guti.socketclient;

/**
 * Created by Guti on 06/09/2017.
 */
public class Usuario {
    private String username;
    private String contrasena;
    private String pin;
    private String admin;

    public Usuario(String username, String contrasena, String pin, String admin) {
        this.username = username;
        this.contrasena = contrasena;
        this.pin = pin;
        this.admin = admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getPin() {
        return pin;
    }

    public String getAdmin() {
        return admin;
    }

    public boolean is_admin() {
        if ("0".compareTo(this.admin) == 0) return true;
        else return false;
    }

    public String toString () {
        return this.username + "-" + this.contrasena;
    }




}
