package com.example.demo.service;

import com.example.demo.controller.PetController;
import com.example.demo.domain.Pet;
import com.example.demo.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void testAddPet() throws Exception {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");

        when(petService.addPet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void testGetPetById() throws Exception {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");

        when(petService.getPetById(1L)).thenReturn(pet);

        mockMvc.perform(get("/api/v3/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    void testUpdatePet() throws Exception {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy Updated");

        when(petService.updatePet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(put("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Buddy Updated"));
    }

    @Test
    void testDeletePet() throws Exception {
        doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete("/api/v3/pet/1"))
                .andExpect(status().isOk());
    }

    // Вспомогательный метод для преобразования объекта в JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}