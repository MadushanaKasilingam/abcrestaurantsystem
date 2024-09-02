package com.project.system.service;

import com.project.system.dto.GalleryImageDTO;
import com.project.system.model.GalleryImage;
import com.project.system.repository.GalleryImageRepository;
import com.project.system.request.AddGalleryImageRequest;
import com.project.system.response.GalleryImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GalleryImageServiceImpl implements GalleryImageService {

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Override
    public GalleryImageResponse addImage(AddGalleryImageRequest request) {
        GalleryImage image = new GalleryImage();
        image.setUrl(request.getUrl());
        image.setRestaurantId(request.getRestaurantId());
        galleryImageRepository.save(image);
        return new GalleryImageResponse("Image added successfully.", image);
    }

    @Override
    public GalleryImageResponse updateImage(Long id, AddGalleryImageRequest request) {
        Optional<GalleryImage> optionalImage = galleryImageRepository.findById(id);
        if (optionalImage.isPresent()) {
            GalleryImage image = optionalImage.get();
            image.setUrl(request.getUrl());
            image.setRestaurantId(request.getRestaurantId());
            galleryImageRepository.save(image);
            return new GalleryImageResponse("Image updated successfully.", image);
        } else {
            throw new RuntimeException("Image not found.");
        }
    }

    @Override
    public GalleryImageResponse deleteImage(Long id) {
        Optional<GalleryImage> optionalImage = galleryImageRepository.findById(id);
        if (optionalImage.isPresent()) {
            galleryImageRepository.deleteById(id);
            return new GalleryImageResponse("Image deleted successfully.", null);
        } else {
            throw new RuntimeException("Image not found.");
        }
    }

    @Override
    public GalleryImageResponse getImage(Long id) {
        Optional<GalleryImage> optionalImage = galleryImageRepository.findById(id);
        if (optionalImage.isPresent()) {
            return new GalleryImageResponse("Image retrieved successfully.", optionalImage.get());
        } else {
            throw new RuntimeException("Image not found.");
        }
    }

    @Override
    public List<GalleryImageDTO> getAllImages() {
        return galleryImageRepository.findAll().stream()
                .map(image -> new GalleryImageDTO(image.getId(), image.getUrl(), image.getRestaurantId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GalleryImageDTO> getImagesByRestaurantId(Long restaurantId) {
        return galleryImageRepository.findByRestaurantId(restaurantId).stream()
                .map(image -> new GalleryImageDTO(image.getId(), image.getUrl(), image.getRestaurantId()))
                .collect(Collectors.toList());
    }
}
