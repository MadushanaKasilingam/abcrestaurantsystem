
        const restaurantList = document.getElementById('restaurantList');
        const restaurantModal = document.getElementById('restaurantModal');
        const modalTitle = document.getElementById('modalTitle');
        const saveRestaurantBtn = document.getElementById('saveRestaurantBtn');
        const addRestaurantBtn = document.getElementById('addRestaurantBtn');
        const messageDiv = document.getElementById('message');
        const token = localStorage.getItem('jwt');
    
        const restaurantName = document.getElementById('restaurantName');
        const restaurantDescription = document.getElementById('restaurantDescription');
        const restaurantCuisineType = document.getElementById('restaurantCuisineType');
        const restaurantCountry = document.getElementById('restaurantCountry');
        const restaurantStreetAddress = document.getElementById('restaurantStreetAddress');
        const restaurantCity = document.getElementById('restaurantCity');
        const restaurantStateProvince = document.getElementById('restaurantStateProvince');
        const restaurantPostalCode = document.getElementById('restaurantPostalCode');
        const restaurantMobile = document.getElementById('restaurantMobile');
        const restaurantEmail = document.getElementById('restaurantEmail');
        const restaurantTwitter = document.getElementById('restaurantTwitter');
        const restaurantInstagram = document.getElementById('restaurantInstagram');
        const restaurantOpeningHours = document.getElementById('restaurantOpeningHours');
        const restaurantImages = document.getElementById('restaurantImages');
        const restaurantRegistrationDate = document.getElementById('restaurantRegistrationDate');
        const restaurantOpen = document.getElementById('restaurantOpen');
    
        let isUpdate = false;
        let currentRestaurantId = null;
    
        // Fetch and display all restaurants
        async function loadRestaurants() {
            try {
                const response = await fetch('http://localhost:5786/api/restaurants', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const restaurants = await response.json();
                    restaurantList.innerHTML = '';
                    restaurants.forEach(restaurant => {
                        const restaurantItem = document.createElement('div');
                        restaurantItem.classList.add('restaurant-item');
                        restaurantItem.innerHTML = `
                            <h3>${restaurant.name}</h3>
                            <div class="restaurant-actions">
                                <button class="btn" onclick="editRestaurant(${restaurant.id})">Edit</button>
                                <button class="btn btn-delete" onclick="deleteRestaurant(${restaurant.id})">Delete</button>
                            </div>
                        `;
                        restaurantList.appendChild(restaurantItem);
                    });
                } else {
                    restaurantList.innerHTML = '<p>No restaurants found.</p>';
                }
            } catch (error) {
                console.error('Error loading restaurants:', error);
                restaurantList.innerHTML = '<p>Failed to load restaurants.</p>';
            }
        }
    
        // Open modal to add new restaurant
        addRestaurantBtn.addEventListener('click', () => {
            isUpdate = false;
            currentRestaurantId = null;
            resetForm();
            modalTitle.textContent = 'Add Restaurant';
            restaurantModal.style.display = 'flex';
        });
    
        // Reset form
        function resetForm() {
            restaurantName.value = '';
            restaurantDescription.value = '';
            restaurantCuisineType.value = '';
            restaurantCountry.value = '';
            restaurantStreetAddress.value = '';
            restaurantCity.value = '';
            restaurantStateProvince.value = '';
            restaurantPostalCode.value = '';
            restaurantMobile.value = '';
            restaurantEmail.value = '';
            restaurantTwitter.value = '';
            restaurantInstagram.value = '';
            restaurantOpeningHours.value = '';
            restaurantImages.value = '';
            restaurantRegistrationDate.value = '';
            restaurantOpen.checked = false;
        }
    
        // Validate form fields
        function validateForm() {
            if (!restaurantName.value || !restaurantDescription.value || !restaurantCuisineType.value || 
                !restaurantCountry.value || !restaurantStreetAddress.value || !restaurantCity.value || 
                !restaurantStateProvince.value || !restaurantPostalCode.value || !restaurantMobile.value || 
                !restaurantEmail.value || !restaurantOpeningHours.value || !restaurantRegistrationDate.value) {
                messageDiv.textContent = 'Please fill in all required fields.';
                messageDiv.classList.add('error-message');
                setTimeout(() => {
                    messageDiv.textContent = '';
                    messageDiv.classList.remove('error-message');
                }, 3000);
                return false;
            }
            return true;
        }
    
        // Save or update restaurant
        saveRestaurantBtn.addEventListener('click', async () => {
            // Validate form before proceeding
            if (!validateForm()) {
                return;
            }
    
            const name = restaurantName.value;
            const description = restaurantDescription.value;
            const cuisineType = restaurantCuisineType.value;
            const address = {
                country: restaurantCountry.value,
                streetAddress: restaurantStreetAddress.value,
                city: restaurantCity.value,
                stateProvince: restaurantStateProvince.value,
                postalCode: restaurantPostalCode.value
            };
            const contactInformation = {
                mobile: restaurantMobile.value,
                email: restaurantEmail.value,
                twitter: restaurantTwitter.value,
                instagram: restaurantInstagram.value
            };
            const openingHours = restaurantOpeningHours.value;
            const images = restaurantImages.value.split(',');
            const registrationDate = restaurantRegistrationDate.value;
            const open = restaurantOpen.checked;
    
            const restaurantData = {
                name,
                description,
                cuisineType,
                address,
                contactInformation,
                openingHours,
                images,
                registrationDate,
                open
            };
    
            try {
                const response = isUpdate
                    ? await fetch(`http://localhost:5786/api/admin/restaurants/${currentRestaurantId}`, {
                          method: 'PUT',
                          headers: {
                              'Content-Type': 'application/json',
                              'Authorization': `Bearer ${token}`
                          },
                          body: JSON.stringify(restaurantData)
                      })
                    : await fetch('http://localhost:5786/api/admin/restaurants', {
                          method: 'POST',
                          headers: {
                              'Content-Type': 'application/json',
                              'Authorization': `Bearer ${token}`
                          },
                          body: JSON.stringify(restaurantData)
                      });
    
                if (response.ok) {
                    // Update the restaurant status if it's an update
                    if (isUpdate) {
                        await fetch(`http://localhost:5786/api/admin/restaurants/${currentRestaurantId}/status`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${token}`
                            },
                            body: JSON.stringify({ open })
                        });
                    }
                    
                    loadRestaurants();
                    restaurantModal.style.display = 'none';
                    messageDiv.textContent = isUpdate
                        ? 'Restaurant updated successfully!'
                        : 'Restaurant added successfully!';
                    messageDiv.classList.add('success-message');
                    setTimeout(() => {
                        messageDiv.textContent = '';
                        messageDiv.classList.remove('success-message');
                    }, 3000);
                } else {
                    const errorData = await response.json();
                    messageDiv.textContent = `Error: ${errorData.message}`;
                    messageDiv.classList.add('error-message');
                    setTimeout(() => {
                        messageDiv.textContent = '';
                        messageDiv.classList.remove('error-message');
                    }, 3000);
                }
            } catch (error) {
                console.error('Error saving restaurant:', error);
            }
        });

// Edit restaurant
async function editRestaurant(id) {
    isUpdate = true;
    currentRestaurantId = id;
    modalTitle.textContent = 'Update Restaurant';
    restaurantModal.style.display = 'flex';

    try {
        const response = await fetch(`http://localhost:5786/api/admin/restaurants/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const restaurant = await response.json();
            restaurantName.value = restaurant.name;
            restaurantDescription.value = restaurant.description;
            restaurantCuisineType.value = restaurant.cuisineType;
            restaurantCountry.value = restaurant.address.country;
            restaurantStreetAddress.value = restaurant.address.streetAddress;
            restaurantCity.value = restaurant.address.city;
            restaurantStateProvince.value = restaurant.address.stateProvince;
            restaurantPostalCode.value = restaurant.address.postalCode;
            restaurantMobile.value = restaurant.contactInformation.mobile;
            restaurantEmail.value = restaurant.contactInformation.email;
            restaurantTwitter.value = restaurant.contactInformation.twitter;
            restaurantInstagram.value = restaurant.contactInformation.instagram;
            restaurantOpeningHours.value = restaurant.openingHours;
            restaurantImages.value = restaurant.images.join(',');
            restaurantRegistrationDate.value = restaurant.registrationDate;
            restaurantOpen.checked = restaurant.open;
        } else {
            console.error('Failed to fetch restaurant details.');
        }
    } catch (error) {
        console.error('Error fetching restaurant details:', error);
    }
}

        // Delete restaurant
        async function deleteRestaurant(id) {
            if (confirm('Are you sure you want to delete this restaurant?')) {
                try {
                    const response = await fetch(`http://localhost:5786/api/admin/restaurants/${id}`, {
                        method: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });

                    if (response.ok) {
                        loadRestaurants();
                        messageDiv.textContent = 'Restaurant deleted successfully!';
                        messageDiv.classList.add('success-message');
                        setTimeout(() => {
                            messageDiv.textContent = '';
                            messageDiv.classList.remove('success-message');
                        }, 3000);
                    } else {
                        console.error('Failed to delete restaurant.');
                    }
                } catch (error) {
                    console.error('Error deleting restaurant:', error);
                }
            }
        }

        // Close modal when clicking outside of it
        window.onclick = function(event) {
            if (event.target === restaurantModal) {
                restaurantModal.style.display = 'none';
            }
        }

        // Load restaurants on page load
        window.onload = loadRestaurants;
    