package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Usuario;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    boolean existsByUsername(String username);
}
