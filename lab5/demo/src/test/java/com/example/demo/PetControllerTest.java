package com.example.demo;

import com.example.demo.controller.PetController;
import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet(1L, "Buddy", new Category(101L, "Dog"), null, "Available");
    }

    @Test
    void shouldCreatePet() throws Exception {
        when(petService.addPet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void shouldUpdatePet() throws Exception {
        when(petService.updatePet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(put("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void shouldFindPetById() throws Exception {
        when(petService.getPetById("1")).thenReturn(pet);

        mockMvc.perform(get("/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void shouldDeletePet() throws Exception {
        doNothing().when(petService).deletePet("1");

        mockMvc.perform(delete("/pet/1"))
                .andExpect(status().isOk());

        verify(petService).deletePet("1");
    }

}
