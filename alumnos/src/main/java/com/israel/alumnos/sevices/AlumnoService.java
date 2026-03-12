package com.israel.alumnos.sevices;

import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Alumno> obtenerTodos(){
        return alumnoRepository.findAll();
    }
    public Optional<Alumno> obtenerPorId(Long id){
        return alumnoRepository.findById(id);
    }
    public Alumno guardarAlumno(Alumno alumno){
        return alumnoRepository.save(alumno);
    }
    public Optional<Alumno> actualizarAlumno (long id, Alumno alumnoDetalles){
        return alumnoRepository.findById(id).map(alumnoExitente -> {
            alumnoExitente.setNombre(alumnoDetalles.getNombre());
            alumnoExitente.setApellido(alumnoDetalles.getApellido());
            alumnoExitente.setEmail(alumnoDetalles.getEmail());
            alumnoExitente.setNumeroControl(alumnoDetalles.getNumeroControl());
            alumnoExitente.setTelefono(alumnoDetalles.getTelefono());
            alumnoExitente.setCarrera(alumnoDetalles.getCarrera());
            alumnoExitente.setImagenURL(alumnoDetalles.getImagenURL());
            return alumnoRepository.save(alumnoExitente);
        });
    }
    public void eliminarAlumno(long id){
        alumnoRepository.deleteById(id);
    }
}

