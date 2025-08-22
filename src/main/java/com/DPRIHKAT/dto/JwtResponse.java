package com.DPRIHKAT.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String login;
    private String role;

    public JwtResponse(String accessToken, String login, String role) {
        this.token = accessToken;
        this.login = login;
        this.role = role;
    }

    // Getters and setters
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
