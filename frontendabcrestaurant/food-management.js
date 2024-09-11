
        const foodList = document.getElementById('foodList');
        const foodModal = document.getElementById('foodModal');
        const modalTitle = document.getElementById('modalTitle');
        const saveFoodBtn = document.getElementById('saveFoodBtn');
        const addFoodBtn = document.getElementById('addFoodBtn');
        const messageDiv = document.getElementById('message');
        const token = localStorage.getItem('jwt');
        
        const restaurantSelect = document.getElementById('restaurantSelect');
        const categorySelect = document.getElementById('categorySelect');
        const foodName = document.getElementById('foodName');
        const foodPrice = document.getElementById('foodPrice');
        const foodDescription = document.getElementById('foodDescription');
        const foodImages = document.getElementById('foodImages');
        const foodAvailable = document.getElementById('foodAvailable');
        const isVegetarian = document.getElementById('isVegetarian');
        const isSeasonal = document.getElementById('isSeasonal');
        const createdDate = document.getElementById('createdDate');
        
        let isUpdate = false;
        let currentFoodId = null;

         // Close modal when close button is clicked
         closeModalBtn.addEventListener('click', () => {
            foodModal.style.display = 'none';
        });

        // Close modal when clicked outside modal content
        window.addEventListener('click', (event) => {
            if (event.target === foodModal) {
                foodModal.style.display = 'none';
            }
        });
        
        // Fetch and display all food items grouped by restaurant and category
        async function loadFoods() {
            try {
                const response = await fetch('http://localhost:5786/api/food', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
        
                const data = await response.json();
        
                foodList.innerHTML = '';
        
                if (Array.isArray(data) && data.length > 0) {
                    const restaurants = {};
        
                    // Group foods by restaurant and category
                    data.forEach(food => {
                        if (!restaurants[food.restaurant.id]) {
                            restaurants[food.restaurant.id] = {
                                name: food.restaurant.name,
                                categories: {}
                            };
                        }
        
                        if (!restaurants[food.restaurant.id].categories[food.foodCategory.id]) {
                            restaurants[food.restaurant.id].categories[food.foodCategory.id] = {
                                name: food.foodCategory.name,
                                foods: []
                            };
                        }
        
                        restaurants[food.restaurant.id].categories[food.foodCategory.id].foods.push(food);
                    });
        
                    // Render grouped foods
                    for (const restaurantId in restaurants) {
                        const restaurantSection = document.createElement('div');
                        restaurantSection.classList.add('restaurant-section');
                        restaurantSection.innerHTML = `<h2>${restaurants[restaurantId].name}</h2>`;
        
                        const categories = restaurants[restaurantId].categories;
                        for (const categoryId in categories) {
                            const categorySection = document.createElement('div');
                            categorySection.classList.add('category-section');
                            categorySection.innerHTML = `<h3>${categories[categoryId].name}</h3>`;
        
                            categories[categoryId].foods.forEach(food => {
                                const foodItem = document.createElement('div');
                                foodItem.classList.add('food-item');
                                foodItem.innerHTML = `
                                    <div>
                                        <h4>${food.name}</h4>
                                        <p>${food.description}</p>
                                        <p>Price: rs${food.price}</p>
                                        <p>Available: ${food.available ? 'Yes' : 'No'}</p>
                                        <p>Vegetarian: ${food.vegetarian ? 'Yes' : 'No'}</p>
                                        <p>Seasonal: ${food.seasonal ? 'Yes' : 'No'}</p>
                                    </div>
                                    <div class="food-actions">
                                        <button class="btn" onclick="editFood(${food.id})">Edit</button>
                                        <button class="btn btn-delete" onclick="deleteFood(${food.id})">Delete</button>
                                    </div>
                                `;
                                categorySection.appendChild(foodItem);
                            });
        
                            restaurantSection.appendChild(categorySection);
                        }
        
                        foodList.appendChild(restaurantSection);
                    }
                } else {
                    foodList.innerHTML = '<p>No food items found.</p>';
                }
            } catch (error) {
                console.error('Error loading foods:', error);
                showErrorMessage('Error loading foods');
            }
        }
        
        // Fetch and display all restaurants
        async function loadRestaurants() {
            try {
                const response = await fetch('http://localhost:5786/api/restaurants', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
        
                const data = await response.json();
                console.log(data); // Check what data is being returned
        
                restaurantSelect.innerHTML = '<option value="">Select Restaurant</option>';
                data.forEach(restaurant => {
                    const option = document.createElement('option');
                    option.value = restaurant.id;
                    option.textContent = restaurant.name;
                    restaurantSelect.appendChild(option);
                });
            } catch (error) {
                console.error('Error loading restaurants:', error);
                showErrorMessage('Error loading restaurants');
            }
        }
        
        // Fetch and display categories based on selected restaurant
        async function loadCategories(restaurantId) {
            try {
                const response = await fetch(`http://localhost:5786/api/categories/restaurant/${restaurantId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
        
                const data = await response.json();
                console.log(data); // Check what data is being returned
        
                categorySelect.innerHTML = '<option value="">Select Category</option>';
                data.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    categorySelect.appendChild(option);
                });
            } catch (error) {
                console.error('Error loading categories:', error);
                showErrorMessage('Error loading categories');
            }
        }
        
        // Add or update food item
        async function saveFood() {
            // Validation check for empty fields
            if (!restaurantSelect.value || !categorySelect.value || !foodName.value || !foodPrice.value || !foodDescription.value || !foodImages.value || !createdDate.value) {
                showErrorMessage('Please fill in all the required fields.');
                return;
            }
        
            const food = {
                restaurantId: parseInt(restaurantSelect.value, 10),
                foodCategoryId: parseInt(categorySelect.value, 10),
                name: foodName.value,
                description: foodDescription.value,
                price: parseFloat(foodPrice.value),
                images: foodImages.value.split(',').map(url => url.trim()),
                available: foodAvailable.checked,
                is_vegetarian: isVegetarian.checked,
                is_seasonal: isSeasonal.checked,
                createdDate: createdDate.value,
            };
        
            const url = isUpdate ? `http://localhost:5786/api/admin/food/${currentFoodId}` : 'http://localhost:5786/api/admin/food';
            const method = isUpdate ? 'PUT' : 'POST';
        
            try {
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(food)
                });
        
                const responseData = await response.json();
                console.log('Response Data:', responseData); // Debugging: Check the response data
        
                if (response.ok) {
                    showSuccessMessage(isUpdate ? 'Food updated successfully' : 'Food added successfully');
                    loadFoods();
                    closeModal();
                } else {
                    console.error('Error Response:', responseData); // Debugging: Log the error response
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
            } catch (error) {
                console.error('Error saving food:', error);
                showErrorMessage('Error saving food');
            }
        }
        
        // Delete food item
        async function deleteFood(foodId) {
            if (!confirm('Are you sure you want to delete this food item?')) {
                return;
            }
        
            try {
                const response = await fetch(`http://localhost:5786/api/admin/food/${foodId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
        
                if (response.ok) {
                    showSuccessMessage('Food deleted successfully');
                    loadFoods();
                } else {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
            } catch (error) {
                console.error('Error deleting food:', error);
                showErrorMessage('Error deleting food');
            }
        }
        
        // Show success message
        function showSuccessMessage(message) {
            messageDiv.innerHTML = `<div class="success-message">${message}</div>`;
            setTimeout(() => messageDiv.innerHTML = '', 3000);
        }
        
        // Show error message
        function showErrorMessage(message) {
            messageDiv.innerHTML = `<div class="error-message">${message}</div>`;
            setTimeout(() => messageDiv.innerHTML = '', 3000);
        }
        
        // Open modal for adding/updating food
        function openModal() {
            foodModal.style.display = 'flex';
        }
        
        // Close modal
        function closeModal() {
            foodModal.style.display = 'none';
            resetForm();
        }
        
        // Reset form fields
        function resetForm() {
            foodName.value = '';
            foodDescription.value = '';
            foodPrice.value = '';
            foodImages.value = '';
            foodAvailable.checked = false;
            isVegetarian.checked = false;
            isSeasonal.checked = false;
            createdDate.value = '';
            categorySelect.innerHTML = '<option value="">Select Category</option>';
            restaurantSelect.value = '';
            isUpdate = false;
            currentFoodId = null;
            modalTitle.textContent = 'Add Food';
        }
        
        // Edit food item
        function editFood(foodId) {
            openModal();
            modalTitle.textContent = 'Update Food';
            isUpdate = true;
            currentFoodId = foodId;
        
            // Fetch food details and populate form
            fetch(`http://localhost:5786/api/food/${foodId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(response => response.json())
                .then(food => {
                    restaurantSelect.value = food.restaurant.id;
                    loadCategories(food.restaurant.id);
                    categorySelect.value = food.foodCategory.id;
                    foodName.value = food.name;
                    foodDescription.value = food.description;
                    foodPrice.value = food.price;
                    foodImages.value = food.images.join(', ');
                    foodAvailable.checked = food.available;
                    isVegetarian.checked = food.is_vegetarian;
                    isSeasonal.checked = food.is_seasonal;
                    createdDate.value = food.createdDate;
                })
                .catch(error => {
                    console.error('Error loading food details:', error);
                    showErrorMessage('Error loading food details');
                });
        }
        
        // Load restaurants when the page loads
        document.addEventListener('DOMContentLoaded', () => {
            loadFoods();
            loadRestaurants();
        
            // Load categories based on selected restaurant
            restaurantSelect.addEventListener('change', () => {
                const restaurantId = restaurantSelect.value;
                if (restaurantId) {
                    loadCategories(restaurantId);
                } else {
                    categorySelect.innerHTML = '<option value="">Select Category</option>';
                }
            });
        
            // Handle form submission
            saveFoodBtn.addEventListener('click', saveFood);
            addFoodBtn.addEventListener('click', openModal);
        });
       