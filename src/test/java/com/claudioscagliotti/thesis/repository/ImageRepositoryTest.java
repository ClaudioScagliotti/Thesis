package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.ImageEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(classes = ThesisApplication.class)
class ImageRepositoryTest {

    @Autowired
    private ImageRepository repository;
    private ImageEntity newImage;
    @BeforeEach
    void setUp() {
        newImage = new ImageEntity();
        newImage.setUrl("https://example.com/image.jpg");
        newImage.setDescription("A sample image");
        newImage.setBase64Data("base64EncodedString");

        ImageEntity savedImage = repository.save(newImage);
    }

    @AfterEach
    void tearDown() {
        repository.delete(newImage);
    }

    @Test
    void shouldSaveNewImage() {
        ImageEntity newImage = new ImageEntity();
        newImage.setUrl("https://example.com/image.jpg");
        newImage.setDescription("A sample image");
        newImage.setBase64Data("base64EncodedString");

        ImageEntity savedImage = repository.save(newImage);

        BDDAssertions.then(savedImage.getId()).isNotNull();
        BDDAssertions.then(savedImage.getUrl()).isEqualTo("https://example.com/image.jpg");
    }

    @Test
    void shouldFindAllImages() {
        List<ImageEntity> list = repository.findAll();
        BDDAssertions.then(list).isNotNull();
        BDDAssertions.then(list.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldDeleteImageById() {
        ImageEntity imageToDelete = repository.findAll().get(0);
        int size = repository.findAll().size();
        repository.deleteById(imageToDelete.getId());

        List<ImageEntity> remainingImages = repository.findAll();
        BDDAssertions.then(remainingImages.size()).isEqualTo(size-1);
        BDDAssertions.then(remainingImages.contains(imageToDelete)).isFalse();
    }

    @Test
    void shouldUpdateImage() {
        ImageEntity imageToUpdate = repository.findAll().get(0);
        imageToUpdate.setDescription("Updated description");

        ImageEntity updatedImage = repository.save(imageToUpdate);

        BDDAssertions.then(updatedImage.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void shouldFindImageById() {
        ImageEntity image = repository.findAll().get(0);
        Optional<ImageEntity> foundImage = repository.findById(image.getId());

        BDDAssertions.then(foundImage).isPresent();
        BDDAssertions.then(foundImage.get().getId()).isEqualTo(image.getId());
    }
}
