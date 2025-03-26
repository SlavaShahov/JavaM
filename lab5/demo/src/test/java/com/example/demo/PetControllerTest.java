package com.example.demo;

import com.example.demo.controller.PetController;
import com.example.demo.model.Pet;
import com.example.demo.service.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @Test
    void createPet_ValidPet_ReturnsCreatedPet() {
        Pet inputPet = new Pet(1L, "Rex", null, null, "available");
        Pet savedPet = new Pet(1L, "Rex", null, null, "available");

        when(petService.addPet(any(Pet.class))).thenReturn(savedPet);

        Pet result = petController.createPet(inputPet);

        assertEquals(savedPet, result);
        verify(petService).addPet(inputPet);
    }

    @Test
    void updatePet_ValidPet_ReturnsUpdatedPet() {
        Pet inputPet = new Pet(1L, "Rex", null, null, "available");
        Pet updatedPet = new Pet(1L, "Rex Updated", null, null, "available");

        when(petService.updatePet(any(Pet.class))).thenReturn(updatedPet);

        Pet result = petController.updatePet(inputPet);

        assertEquals(updatedPet, result);
        verify(petService).updatePet(inputPet);
    }

    @Test
    void findPet_ValidId_ReturnsPet() {
        String petId = "1";
        Pet expectedPet = new Pet(1L, "Rex", null, null, "available");

        when(petService.getPetById(petId)).thenReturn(expectedPet);

        Pet result = petController.findPet(petId);

        assertEquals(expectedPet, result);
        verify(petService).getPetById(petId);
    }

    @Test
    void deletePet_ValidId_DeletesPet() {
        String petId = "1";

        petController.deletePet(petId);

        verify(petService).deletePet(petId);
    }
}