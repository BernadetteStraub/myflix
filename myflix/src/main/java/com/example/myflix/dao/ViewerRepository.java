package com.example.myflix.dao;

import com.example.myflix.model.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewerRepository extends JpaRepository<Viewer, Long> {

    boolean existsByUsername(String username); //for Admin config

    Optional<Viewer> findOneByUsername(String username); //ONLY used in SecurityConfig
}
