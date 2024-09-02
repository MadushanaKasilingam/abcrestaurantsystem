package com.project.system.service;

import com.project.system.model.Address;
import com.project.system.model.Restaurant;
import com.project.system.repository.AddressRepository;
import com.project.system.repository.RestaurantRepository;
import com.project.system.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req) {
        if (restaurantExists(req)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Restaurant with the same details already exists"
            );
        }

        Address address = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOpen(true);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (updatedRestaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        }
        if (updatedRestaurant.getDescription() != null) {
            restaurant.setDescription(updatedRestaurant.getDescription());
        }
        if (updatedRestaurant.getName() != null) {
            restaurant.setName(updatedRestaurant.getName());
        }

        if (updatedRestaurant.getAddress() != null) {
            Address existingAddress = restaurant.getAddress();
            Address newAddress = updatedRestaurant.getAddress();
            if (existingAddress != null) {
                existingAddress.setStreetAddress(newAddress.getStreetAddress());
                existingAddress.setCity(newAddress.getCity());
                existingAddress.setStateProvince(newAddress.getStateProvince());
                existingAddress.setPostalCode(newAddress.getPostalCode());
                existingAddress.setCountry(newAddress.getCountry());
                addressRepository.save(existingAddress); // Save updated address
            } else {
                Address savedAddress = addressRepository.save(newAddress); // Save new address
                restaurant.setAddress(savedAddress);
            }
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        // Remove the associated address if it exists
        Address address = restaurant.getAddress();
        if (address != null) {
            addressRepository.delete(address);
        }

        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> opt = restaurantRepository.findById(id);
        if (opt.isEmpty()) {
            throw new Exception("Restaurant not found with id " + id);
        }
        return opt.get();
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }

    private boolean restaurantExists(CreateRestaurantRequest req) {
        return restaurantRepository.existsByNameAndAddress(
                req.getName(), req.getAddress().getStreetAddress(), req.getAddress().getCity(),
                req.getAddress().getStateProvince(), req.getAddress().getPostalCode(),
                req.getAddress().getCountry()
        );
    }
}
