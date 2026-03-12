package com.israel.alumnos.controllers;

import java.util.List;
import java.util.Optional;

import com.israel.alumnos.sevices.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    // Metodo get para traer todos los alumnos de la base de datos
    @GetMapping("/traer-alumnos")
    public List<Alumno> TraerAlumnos() {

        return alumnoService.obtenerTodos();
    }

    @GetMapping("/traer-alumno/{id}")
    public ResponseEntity<Alumno> TraerUnAlumno(@PathVariable Long id) {
        Optional<Alumno> alumno = alumnoService.obtenerPorId(id);
        return alumno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Metodo para insertar un alumno en la base de datos
    @PostMapping("/insertar-alumnos")
    public Alumno insertarAlumno(@RequestBody Alumno alumno) {
        return alumnoService.guardarAlumno(alumno);

    }

    // Metodo para editar un alumno en la base de datos
    @PutMapping("/editar-alumnos/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumno) {
        Optional<Alumno> actualizado = alumnoService.actualizarAlumno(id,alumno);
        return actualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // metodo para eliminar un alumno de la base de datos
    @DeleteMapping("/eliminar-alumnos/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id){
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.ok().build();
    }

}
