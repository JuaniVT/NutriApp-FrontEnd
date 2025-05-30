package com.NutriApp.NutriApp.modelo.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROL_ADMIN("ADMIN"), ROL_CLIENT("CLIENT");

    private String rol;

    Authority(String rol) {
        this.rol = rol;
    }

    @Override
    public String getAuthority() {
        // Aquí devolvemos el rol con el prefijo "ROLE_" que Spring Security usa por convención
        return "ROLE_" + rol;
    }
}
