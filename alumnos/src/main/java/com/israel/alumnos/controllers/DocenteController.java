package com.israel.alumnos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israel.alumnos.model.Docente;
import com.israel.alumnos.repository.DocenteRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    @GetMapping("/traer-docentes")
    public List<Docente> TraerDocentes() {
        return docenteRepository.findAll();
    }

    @GetMapping("/traer-docente/{id}")
    public ResponseEntity<Docente> TraerUnDocente(@PathVariable Long id) {
        return docenteRepository.findById(id)
                .map(docente -> ResponseEntity.ok(docente))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/insertar-docentes")
    public Docente insertarDocente(@RequestBody Docente docente) {
        return docenteRepository.save(docente);
    }

    @PutMapping("/editar-docentes/{id}")
    public ResponseEntity<Docente> actualizarDocente(@PathVariable Long id, @RequestBody Docente docente) {
        return docenteRepository.findById(id).map(docenteExistente -> {
            docenteExistente.setNombre(docente.getNombre());
            docenteExistente.setApellido(docente.getApellido());
            docenteExistente.setEmail(docente.getEmail());
            docenteExistente.setNumeroEmpleado(docente.getNumeroEmpleado());
            docenteExistente.setTelefono(docente.getTelefono());
            docenteExistente.setArea(docente.getArea());
            docenteExistente.setImagenURL(docente.getImagenURL());
            Docente actualizado = docenteRepository.save(docenteExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar-docentes/{id}")
    public void eliminarDocente(@PathVariable Long id) {
        docenteRepository.deleteById(id);
    }
}