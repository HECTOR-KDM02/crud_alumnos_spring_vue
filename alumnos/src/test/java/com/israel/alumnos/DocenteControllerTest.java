package com.israel.alumnos;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import com.israel.alumnos.controllers.DocenteController;
import com.israel.alumnos.model.Docente;
import com.israel.alumnos.repository.DocenteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DocenteController.class)
public class DocenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocenteRepository docenteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void debeTraerTodosLosDocentes() throws Exception {

        Docente d1 = new Docente();
        d1.setId(1L);
        d1.setNombre("Ana");
        d1.setArea("Sistemas");

        Docente d2 = new Docente();
        d2.setId(2L);
        d2.setNombre("Luis");
        d2.setArea("Matematicas");

        when(docenteRepository.findAll())
                .thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/docentes/traer-docentes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Ana")));
    }

    @Test
    public void debeInsertarUnDocente() throws Exception {

        Docente nuevo = new Docente();
        nuevo.setNombre("Hector");
        nuevo.setNumeroEmpleado("D010");

        when(docenteRepository.save(org.mockito.ArgumentMatchers.any(Docente.class)))
                .thenReturn(nuevo);

        mockMvc.perform(post("/docentes/insertar-docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Hector")))
                .andExpect(jsonPath("$.numeroEmpleado", is("D010")));
    }

    @Test
    public void debeEliminarUnDocente() throws Exception {
        long idParaEliminar = 1L;

        mockMvc.perform(delete("/docentes/eliminar-docentes/{id}", idParaEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(docenteRepository, times(1)).deleteById(idParaEliminar);
    }

    @Test
    public void debeEditarUnDocente() throws Exception {
        long idParaEditar = 1L;

        Docente docenteExistente = new Docente();
        docenteExistente.setId(idParaEditar);
        docenteExistente.setNombre("Hector");
        docenteExistente.setNumeroEmpleado("D010");
        docenteExistente.setArea("Telecom");

        Docente docenteEditado = new Docente();
        docenteEditado.setId(idParaEditar);
        docenteEditado.setNombre("Hector Modificado");
        docenteEditado.setNumeroEmpleado("D010");
        docenteEditado.setArea("Telecom");

        when(docenteRepository.findById(idParaEditar))
                .thenReturn(java.util.Optional.of(docenteExistente));

        when(docenteRepository.save(org.mockito.ArgumentMatchers.any(Docente.class)))
                .thenReturn(docenteEditado);

        mockMvc.perform(put("/docentes/editar-docentes/{id}", idParaEditar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docenteEditado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Hector Modificado")))
                .andExpect(jsonPath("$.numeroEmpleado", is("D010")));

        verify(docenteRepository, times(1)).findById(idParaEditar);
        verify(docenteRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Docente.class));
    }
}