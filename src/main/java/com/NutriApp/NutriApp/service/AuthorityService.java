package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.modelo.Authority;
import com.NutriApp.NutriApp.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public void insertar (Authority authority) throws AuthorityInvalidaException {
        if (authorityRepository.existsById(authority.getUsername())){
            throw new AuthorityInvalidaException("El authority ya existe con el username = " +authority.getUsername());
        }

        authorityRepository.save(authority);
    }
}
