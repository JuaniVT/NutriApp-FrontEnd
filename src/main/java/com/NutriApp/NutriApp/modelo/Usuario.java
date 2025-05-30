package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Usuario implements UserDetails {

    @Id
    @Column(length = 50)
    private String username;  // puede ser email

    private String password;

    private boolean enabled; // este campo es obligatorio en tu tabla SQL

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username"))
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Set<Authority> roles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
