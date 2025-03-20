package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.model.Tag;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.TagRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PetRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetRepository petRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    void testCategoryRepositoryFindByName() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Dogs");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findByName("Dogs");
        assertTrue(foundCategory.isPresent());
        assertEquals("Dogs", foundCategory.get().getName());
    }

    @Test
    void testTagRepositoryFindByName() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Friendly");
        tagRepository.save(tag);

        Optional<Tag> foundTag = tagRepository.findByName("Friendly");
        assertTrue(foundTag.isPresent());
        assertEquals("Friendly", foundTag.get().getName());
    }

    @Test
    void testPetRepositorySaveAndFind() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cats");
        category = categoryRepository.save(category);

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Playful");
        tag = tagRepository.save(tag);

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Whiskers");
        pet.setCategory(category);
        pet.setTags(List.of(tag));

        pet = petRepository.save(pet);
        Optional<Pet> foundPet = petRepository.findById(pet.getId());

        assertTrue(foundPet.isPresent());
        assertEquals("Whiskers", foundPet.get().getName());
        assertEquals("Cats", foundPet.get().getCategory().getName());
        assertEquals(1, foundPet.get().getTags().size());
        assertEquals("Playful", foundPet.get().getTags().get(0).getName());
    }
}

