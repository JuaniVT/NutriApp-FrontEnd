package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository <Authority, String> {
}
