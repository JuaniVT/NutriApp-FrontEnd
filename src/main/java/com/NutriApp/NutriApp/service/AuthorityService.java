package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.modelo.enums.Role;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public void insertar (Authority authority) throws AuthorityInvalidaException {
        if (authorityRepository.existsById(authority.getId())){
            throw new AuthorityInvalidaException("El authority ya existe con el id = " +authority.getId());
        }

        authorityRepository.save(authority);
    }


    @PreAuthorize("hasRole('ADMIN')")   //le agrego seguridad al service por las dudas
    public void cambiaRol_A_ADMIN (int id) throws AuthorityInvalidaException{
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);

        if (optionalAuthority.isEmpty()){
            throw new AuthorityInvalidaException("El authority no existe con el username = " +id);
        }


        if (optionalAuthority.get().getAuthority().equals(Role.ROL_ADMIN)) {
            throw new AuthorityInvalidaException("El authority ya tiene el rol al cual desea cambiar");
        }

        authorityRepository.cambiarRol_A_ADMIN(id);
    }

    @PreAuthorize("hasRole('ADMIN')")   //le agrego seguridad al service por las dudas
    public void cambiaRol_A_CLIENTE (int id) throws AuthorityInvalidaException{
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);

        if (optionalAuthority.isEmpty()){
            throw new AuthorityInvalidaException("El authority no existe con el username = " +id);
        }


        if (optionalAuthority.get().getAuthority().equals(Role.ROL_CLIENT)) {
            throw new AuthorityInvalidaException("El authority ya tiene el rol al cual desea cambiar");
        }

        authorityRepository.cambiarRol_A_CLIENTE(id);
    }
}
