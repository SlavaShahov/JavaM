package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PutMapping
    public Pet updatePet(@RequestBody @Valid Pet pet) {
        return petService.updatePet(pet);
    }

    @PostMapping
    public Pet createPet(@RequestBody @Valid Pet pet) {
        return petService.addPet(pet);
    }

    @GetMapping("/{petId}")
    public Pet findPet(@Valid @PathVariable String petId) {
        return petService.getPetById(petId);
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@Valid @PathVariable String petId) {
        petService.deletePet(petId);
    }

}
