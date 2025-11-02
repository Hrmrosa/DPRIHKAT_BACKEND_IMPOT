package com.DPRIHKAT.security;

import com.DPRIHKAT.entity.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String login;

    private String role;

    @JsonIgnore
    private String motDePasse;

    private boolean premierConnexion;

    private boolean bloque;

    public UserDetailsImpl(UUID id, String login, String motDePasse, String role, boolean premierConnexion, boolean bloque) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.premierConnexion = premierConnexion;
        this.bloque = bloque;
    }

    public static UserDetailsImpl build(Utilisateur utilisateur) {
        return new UserDetailsImpl(
                utilisateur.getId(),
                utilisateur.getLogin(),
                utilisateur.getMotDePasse(),
                utilisateur.getRole().name(),
                utilisateur.isPremierConnexion(),
                utilisateur.isBloque()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !bloque;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean isPremierConnexion() {
        return premierConnexion;
    }

    public void setPremierConnexion(boolean premierConnexion) {
        this.premierConnexion = premierConnexion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}