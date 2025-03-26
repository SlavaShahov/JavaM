package com.example.demo;

import com.example.demo.controller.InvalidInputException;
import com.example.demo.controller.PetNotFoundException;
import com.example.demo.controller.ValidationException;
import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.model.Tag;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void addPet_ValidPet_ReturnsSavedPet() {
        Pet pet = new Pet(1L, "Rex", new Category(1L, "Dogs"), null, "available");

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(pet.getCategory());
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet result = petService.addPet(pet);

        assertEquals(pet, result);
        verify(categoryRepository).findByName("Dogs");
        verify(categoryRepository).save(pet.getCategory());
        verify(petRepository).save(pet);
    }

    @Test
    void addPet_InvalidPet_ThrowsValidationException() {
        Pet pet = new Pet(1L, "", new Category(1L, "Dogs"), null, "available");

        assertThrows(ValidationException.class, () -> petService.addPet(pet));
    }

    @Test
    void updatePet_ExistingPet_ReturnsUpdatedPet() {
        Pet existingPet = new Pet(1L, "Rex", new Category(1L, "Dogs"), null, "available");
        Pet updatedPet = new Pet(1L, "Rex Updated", new Category(1L, "Dogs"), null, "available");

        when(petRepository.findById(anyLong())).thenReturn(Optional.of(existingPet));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(existingPet.getCategory()));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        Pet result = petService.updatePet(updatedPet);

        assertEquals(updatedPet, result);
        verify(petRepository).findById(1L);
        verify(petRepository).save(updatedPet);
    }

    @Test
    void updatePet_NonExistingPet_ThrowsPetNotFoundException() {
        Pet pet = new Pet(1L, "Rex", new Category(1L, "Dogs"), null, "available");

        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.updatePet(pet));
    }

    @Test
    void getPetById_ValidId_ReturnsPet() {
        Long petId = 1L;
        Pet expectedPet = new Pet(petId, "Rex", null, null, "available");

        when(petRepository.findById(petId)).thenReturn(Optional.of(expectedPet));

        Pet result = petService.getPetById("1");

        assertEquals(expectedPet, result);
        verify(petRepository).findById(petId);
    }

    @Test
    void getPetById_InvalidIdFormat_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> petService.getPetById("invalid"));
    }

    @Test
    void getPetById_NonExistingId_ThrowsPetNotFoundException() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById("1"));
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        doNothing().when(petRepository).deleteById(1L);

        petService.deletePet("1");

        verify(petRepository).deleteById(1L);
    }

    @Test
    void deletePet_shouldThrowWhenInvalidId() {
        assertThrows(InvalidInputException.class, () -> petService.deletePet("abc"));
    }


    @Test
    void validateAndParsePetId_ValidId_ReturnsLong() {
        Long result = petService.validateAndParsePetId("123");
        assertEquals(123L, result);
    }

    @Test
    void validateAndParsePetId_InvalidId_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> petService.validateAndParsePetId("abc"));
    }
}