package com.project.system.controller;

import com.project.system.dto.GalleryImageDTO;
import com.project.system.request.AddGalleryImageRequest;
import com.project.system.response.GalleryImageResponse;
import com.project.system.service.GalleryImageService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/gallery")
@Validated
public class GalleryImageController {

    @Autowired
    private GalleryImageService galleryImageService;

    @PostMapping
    public ResponseEntity<Map<String, String>> addImage(@Valid @RequestBody AddGalleryImageRequest request) {
        GalleryImageResponse response = galleryImageService.addImage(request);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", response.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateImage(@PathVariable Long id, @Valid @RequestBody AddGalleryImageRequest request) {
        GalleryImageResponse response = galleryImageService.updateImage(id, request);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", response.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable Long id) {
        GalleryImageResponse response = galleryImageService.deleteImage(id);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", response.getMessage());
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryImageResponse> getImage(@PathVariable Long id) {
        GalleryImageResponse response = galleryImageService.getImage(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GalleryImageDTO>> getAllImages() {
        List<GalleryImageDTO> images = galleryImageService.getAllImages();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<GalleryImageDTO>> getImagesByRestaurantId(@PathVariable Long restaurantId) {
        List<GalleryImageDTO> images = galleryImageService.getImagesByRestaurantId(restaurantId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage()
                ));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
