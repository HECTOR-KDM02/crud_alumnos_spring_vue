package com.israel.alumnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.israel.alumnos.model.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
}