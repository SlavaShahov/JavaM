package com.example.demo.service;

import com.example.demo.domain.Pet;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPet() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        when(petRepository.save(pet)).thenReturn(pet);

        Pet savedPet = petService.addPet(pet);

        assertNotNull(savedPet);
        assertEquals("Buddy", savedPet.getName());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testUpdatePet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(pet)).thenReturn(pet);

        Pet updatedPet = petService.updatePet(pet);

        assertNotNull(updatedPet);
        assertEquals("Buddy", updatedPet.getName());
        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testGetPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet foundPet = petService.getPetById(1L);

        assertNotNull(foundPet);
        assertEquals("Buddy", foundPet.getName());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletePet() {
        Pet pet = new Pet();
        pet.setId(1L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        doNothing().when(petRepository).delete(1L);

        petService.deletePet(1L);

        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).delete(1L);
    }
}