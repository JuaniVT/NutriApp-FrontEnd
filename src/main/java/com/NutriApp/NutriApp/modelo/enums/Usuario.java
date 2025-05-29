package com.NutriApp.NutriApp.modelo.enums;

import com.NutriApp.NutriApp.modelo.Persona;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Usuario {

    @Id
    @Column(length = 50)
    private String username;  // puede ser email

    private String password;

    private boolean enabled; // este campo es obligatorio en tu tabla SQL

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username"))
    private List<Authority> roles;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

}
