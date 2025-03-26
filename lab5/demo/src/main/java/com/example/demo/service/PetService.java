package com.example.demo.service;

import com.example.demo.controller.InvalidInputException;
import com.example.demo.controller.PetNotFoundException;
import com.example.demo.controller.ValidationException;
import com.example.demo.model.Pet;
import com.example.demo.model.Tag;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.StringUtils;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PetService(PetRepository petRepository,
                      CategoryRepository categoryRepository,
                      TagRepository tagRepository) {
        this.petRepository = petRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Pet addPet(Pet pet) {
        validatePet(pet);

        if (pet.getCategory() != null) {
            categoryRepository.findByName(pet.getCategory().getName())
                    .ifPresentOrElse(
                            pet::setCategory,
                            () -> pet.setCategory(categoryRepository.save(pet.getCategory()))
                    );
        }

        List<Tag> tags = pet.getTags() == null ? new ArrayList<>() : new ArrayList<>(pet.getTags());
        if (!tags.isEmpty()) {
            tags.replaceAll(tag ->
                    tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag))
            );
        }
        pet.setTags(tags);

        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        validatePet(pet);
        petRepository.findById(pet.getId())
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (pet.getCategory() != null) {
            categoryRepository.findByName(pet.getCategory().getName())
                    .ifPresentOrElse(
                            pet::setCategory,
                            () -> pet.setCategory(categoryRepository.save(pet.getCategory()))
                    );
        }

        List<Tag> tags = pet.getTags() == null ? new ArrayList<>() : new ArrayList<>(pet.getTags());
        if (!tags.isEmpty()) {
            tags.replaceAll(tag ->
                    tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag))
            );
        }
        pet.setTags(tags);

        return petRepository.save(pet);
    }

    public Pet getPetById(String petId) {
        Long id = validateAndParsePetId(petId);
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
    }

    public void deletePet(String petId) {
        Long id = validateAndParsePetId(petId);
        petRepository.deleteById(id);
    }

    public Long validateAndParsePetId(String petId) {
        try {
            return Long.valueOf(petId);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid pet ID format: " + petId);
        }
    }

    private void validatePet(Pet pet) {
        Objects.requireNonNull(pet, "Pet cannot be null");
        if (!StringUtils.hasText(pet.getName())) {
            throw new ValidationException("Pet name cannot be empty");
        }
        Objects.requireNonNull(pet.getCategory(), "Pet category cannot be null");
    }
}
