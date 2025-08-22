package com.DPRIHKAT.dto;

public class LoginRequest {
    private String login;
    private String motDePasse;
    private boolean premiereConnexion;

    // Getters and setters
    public String getLogin() {
        return login;
        }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isPremiereConnexion() {
        return premiereConnexion;
    }

    public void setPremiereConnexion(boolean premiereConnexion) {
        this.premiereConnexion = premiereConnexion;
    }
}
