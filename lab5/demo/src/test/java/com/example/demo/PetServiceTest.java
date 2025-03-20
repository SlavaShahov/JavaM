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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


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

    private Pet validPet;
    private Category existingCategory;
    private Tag existingTag;

    @BeforeEach
    void setUp() {
        existingCategory = new Category(1L, "Dogs");
        existingTag = new Tag(1L, "Fluffy");

        validPet = new Pet();
        validPet.setId(1L);
        validPet.setName("Buddy");
        validPet.setCategory(new Category(2L, "Cats"));
        validPet.setTags(List.of(new Tag(2L, "Playful")));
        validPet.setStatus("Available");
    }

    @Test
    void addPet_shouldSaveNewCategoryAndTags() {
        Category newCategory = new Category(null, "Reptiles");
        Tag newTag = new Tag(null, "Scaly");
        validPet.setCategory(newCategory);
        validPet.setTags(List.of(newTag));

        when(categoryRepository.findByName("Reptiles")).thenReturn(Optional.empty());
        when(categoryRepository.save(newCategory)).thenReturn(new Category(3L, "Reptiles"));
        when(tagRepository.findByName("Scaly")).thenReturn(Optional.empty());
        when(tagRepository.save(newTag)).thenReturn(new Tag(3L, "Scaly"));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = petService.addPet(validPet);

        assertNotNull(result);
        assertEquals(3L, validPet.getCategory().getId());
        assertEquals(3L, validPet.getTags().get(0).getId());
    }

    @Test
    void addPet_shouldUseExistingCategoryAndTags() {
        validPet.setCategory(existingCategory);
        validPet.setTags(List.of(existingTag));

        when(categoryRepository.findByName("Dogs")).thenReturn(Optional.of(existingCategory));
        when(tagRepository.findByName("Fluffy")).thenReturn(Optional.of(existingTag));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = petService.addPet(validPet);

        assertEquals(1L, result.getCategory().getId());
        assertEquals(1L, result.getTags().get(0).getId());
    }

    @Test
    void updatePet_shouldThrowWhenPetNotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.updatePet(validPet));
    }



    @Test
    void getPetById_shouldReturnPetWhenExists() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(validPet));

        Pet result = petService.getPetById("1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPetById_shouldThrowWhenInvalidIdFormat() {
        assertThrows(InvalidInputException.class, () -> petService.getPetById("invalid"));
    }

    @Test
    void getPetById_shouldThrowWhenPetNotFound() {
        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById("999"));
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
    void validatePet_shouldThrowWhenNameIsEmpty() {
        validPet.setName("  ");

        assertThrows(ValidationException.class, () -> petService.addPet(validPet));
    }



    @Test
    void updatePet_shouldMergeExistingAndNewTags() {
        validPet.setTags(List.of(existingTag, new Tag(null, "NewTag")));
        when(petRepository.findById(1L)).thenReturn(Optional.of(validPet));
        when(tagRepository.findByName("Fluffy")).thenReturn(Optional.of(existingTag));
        when(tagRepository.findByName("NewTag")).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(new Tag(2L, "NewTag"));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = petService.updatePet(validPet);

        assertEquals(2, result.getTags().size());
        verify(tagRepository).save(argThat(tag -> tag.getName().equals("NewTag")));
    }
}
