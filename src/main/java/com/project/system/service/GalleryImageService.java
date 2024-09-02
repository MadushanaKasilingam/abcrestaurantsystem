package com.project.system.service;

import com.project.system.dto.GalleryImageDTO;
import com.project.system.request.AddGalleryImageRequest;
import com.project.system.response.GalleryImageResponse;

import java.util.List;

public interface GalleryImageService {

    GalleryImageResponse addImage(AddGalleryImageRequest request);

    GalleryImageResponse updateImage(Long id, AddGalleryImageRequest request);

    GalleryImageResponse deleteImage(Long id);

    GalleryImageResponse getImage(Long id);

    List<GalleryImageDTO> getAllImages();

    List<GalleryImageDTO> getImagesByRestaurantId(Long restaurantId);
}
