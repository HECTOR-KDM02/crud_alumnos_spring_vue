package com.israel.alumnos;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.israel.alumnos.controllers.AlumnoController;
import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.sevices.AlumnoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AlumnoController.class)
public class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void debeTraerTodosLosAlumnos() throws Exception {
        Alumno alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("Israel");
        alumno1.setCarrera("Sistemas");

        Alumno alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("Juan");
        alumno2.setCarrera("Informatica");

        when(alumnoService.obtenerTodos()).thenReturn(Arrays.asList(alumno1, alumno2));

        mockMvc.perform(get("/alumnos/traer-alumnos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Israel")))
                .andExpect(jsonPath("$[1].nombre", is("Juan")));

        verify(alumnoService, times(1)).obtenerTodos();
    }

    @Test
    public void debeInsertarUnAlumno() throws Exception {
        Alumno alumnoNuevo = new Alumno();
        alumnoNuevo.setNombre("Hector");
        alumnoNuevo.setNumeroControl("22620040");

        // Stub: el service responde con un Alumno (puedes incluir más campos si quieres)
        when(alumnoService.guardarAlumno(any(Alumno.class))).thenReturn(alumnoNuevo);

        mockMvc.perform(post("/alumnos/insertar-alumnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumnoNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Hector")))
                .andExpect(jsonPath("$.numeroControl", is("22620040")));

        // Verificación estricta del objeto que realmente recibió el service
        ArgumentCaptor<Alumno> captor = ArgumentCaptor.forClass(Alumno.class);
        verify(alumnoService, times(1)).guardarAlumno(captor.capture());

        Alumno enviado = captor.getValue();
        assertEquals("Hector", enviado.getNombre());
        assertEquals("22620040", enviado.getNumeroControl());
    }

    @Test
    public void debeEliminarUnAlumno() throws Exception {
        long idParaEliminar = 1L;

        doNothing().when(alumnoService).eliminarAlumno(idParaEliminar);

        mockMvc.perform(delete("/alumnos/eliminar-alumnos/{id}", idParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(alumnoService, times(1)).eliminarAlumno(idParaEliminar);
    }

    @Test
    public void debeEditarUnAlumno() throws Exception {
        long idParaEditar = 1L;

        Alumno alumnoEditado = new Alumno();
        alumnoEditado.setId(idParaEditar);
        alumnoEditado.setNombre("Hector Modificado");
        alumnoEditado.setNumeroControl("22620040");
        alumnoEditado.setCarrera("Sistemas");

        when(alumnoService.actualizarAlumno(eq(idParaEditar), any(Alumno.class)))
                .thenReturn(Optional.of(alumnoEditado));

        mockMvc.perform(put("/alumnos/editar-alumnos/{id}", idParaEditar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumnoEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Hector Modificado")))
                .andExpect(jsonPath("$.numeroControl", is("22620040")))
                .andExpect(jsonPath("$.carrera", is("Sistemas")));

        // Captura estricta del Alumno que llegó al service en el UPDATE
        ArgumentCaptor<Alumno> captor = ArgumentCaptor.forClass(Alumno.class);
        verify(alumnoService, times(1)).actualizarAlumno(eq(idParaEditar), captor.capture());

        Alumno enviado = captor.getValue();
        assertEquals("Hector Modificado", enviado.getNombre());
        assertEquals("22620040", enviado.getNumeroControl());
        assertEquals("Sistemas", enviado.getCarrera());
    }
}