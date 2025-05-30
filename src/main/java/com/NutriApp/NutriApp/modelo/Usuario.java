package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Usuario implements UserDetails {

    @Id
    @Column(length = 50)
    private String username;  // puede ser email

    private String password;

    private boolean enabled; // este campo es obligatorio en tu tabla SQL

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Authority authority;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
