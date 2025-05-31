package com.NutriApp.NutriApp.modelo;

import com.NutriApp.NutriApp.modelo.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@Table (name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {

    @Id
    @Column (length = 50)
    @NotNull
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn (name = "username")
    private Usuario usuario;
}
