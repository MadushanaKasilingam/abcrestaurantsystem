
        const restaurantList = document.getElementById('restaurantList');
        const categoryModal = document.getElementById('categoryModal');
        const modalTitle = document.getElementById('modalTitle');
        const saveCategoryBtn = document.getElementById('saveCategoryBtn');
        const addCategoryBtn = document.getElementById('addCategoryBtn');
        const messageDiv = document.getElementById('message');
        const token = localStorage.getItem('jwt');
    
        const categoryName = document.getElementById('categoryName');
        const restaurantSelect = document.getElementById('restaurantSelect');
        const categoryIdInput = document.getElementById('categoryId');
    
        let isUpdate = false;
        let currentCategoryId = null;

        addCategoryBtn.addEventListener('click', () => {
            isUpdate = false;
            categoryName.value = '';
            restaurantSelect.value = '';
            categoryIdInput.value = '';
            categoryModal.style.display = 'flex';
        });

        // Close modal when close button is clicked
        closeModalBtn.addEventListener('click', () => {
            categoryModal.style.display = 'none';
        });

        // Close modal when clicked outside modal content
        window.addEventListener('click', (event) => {
            if (event.target === categoryModal) {
                categoryModal.style.display = 'none';
            }
        });
    
        // Fetch and display all restaurants and their categories
        async function loadRestaurantsAndCategories() {
            try {
                const response = await fetch('http://localhost:5786/api/restaurants', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const restaurants = await response.json();
                    restaurantList.innerHTML = '';
                    restaurantSelect.innerHTML = '<option value="">Select Restaurant</option>';
    
                    for (const restaurant of restaurants) {
                        const restaurantItem = document.createElement('div');
                        restaurantItem.classList.add('restaurant-item');
                        restaurantItem.innerHTML = `<h3>${restaurant.name}</h3>`;
    
                        // Fetch categories for each restaurant
                        const categoryResponse = await fetch(`http://localhost:5786/api/categories/restaurant/${restaurant.id}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`
                            }
                        });
                        if (categoryResponse.ok) {
                            const categories = await categoryResponse.json();
                            if (categories.length > 0) {
                                categories.forEach(category => {
                                    const categoryItem = document.createElement('div');
                                    categoryItem.classList.add('category-item');
                                    categoryItem.innerHTML = `
                                        <span>${category.name}</span>
                                        <div class="category-actions">
                                            <button class="btn" onclick="editCategory(${category.id}, ${restaurant.id})">Edit</button>
                                            <button class="btn btn-delete" onclick="deleteCategory(${category.id})">Delete</button>
                                        </div>
                                    `;
                                    restaurantItem.appendChild(categoryItem);
                                });
                            } else {
                                const noCategoryItem = document.createElement('div');
                                noCategoryItem.classList.add('category-item');
                                noCategoryItem.innerHTML = `<span>No categories found.</span>`;
                                restaurantItem.appendChild(noCategoryItem);
                            }
                        } else {
                            const noCategoryItem = document.createElement('div');
                            noCategoryItem.classList.add('category-item');
                            noCategoryItem.innerHTML = `<span>Failed to load categories.</span>`;
                            restaurantItem.appendChild(noCategoryItem);
                        }
    
                        restaurantList.appendChild(restaurantItem);
    
                        // Add restaurant to the select dropdown
                        const option = document.createElement('option');
                        option.value = restaurant.id;
                        option.textContent = restaurant.name;
                        restaurantSelect.appendChild(option);
                    }
                } else {
                    restaurantList.innerHTML = '<p>No restaurants found.</p>';
                }
            } catch (error) {
                console.error('Error loading restaurants and categories:', error);
                restaurantList.innerHTML = '<p>Failed to load restaurants.</p>';
            }
        }
    
        // Open modal to add new category
        addCategoryBtn.addEventListener('click', () => {
            isUpdate = false;
            categoryName.value = '';
            restaurantSelect.value = '';
            categoryIdInput.value = '';
            modalTitle.textContent = 'Add Category';
            categoryModal.style.display = 'flex';
        });
    
        // Save category (Add/Update)
        saveCategoryBtn.addEventListener('click', async () => {
            const name = categoryName.value;
            const restaurantId = parseInt(restaurantSelect.value, 10);
            const categoryId = categoryIdInput.value;
    
            if (!name || isNaN(restaurantId)) {
                messageDiv.innerHTML = '<div class="error-message">All fields are required and restaurant must be selected.</div>';
                return;
            }
    
            try {
                const method = isUpdate ? 'PUT' : 'POST';
                const url = isUpdate ? `http://localhost:5786/api/admin/category/${categoryId}` : 'http://localhost:5786/api/admin/category';
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        name: name,
                        restaurant_id: restaurantId
                    })
                });
    
                if (response.ok) {
                    messageDiv.innerHTML = '<div class="success-message">Category saved successfully.</div>';
                    categoryModal.style.display = 'none';
                    loadRestaurantsAndCategories();
                } else {
                    const errorData = await response.json();
                    messageDiv.innerHTML = `<div class="error-message">${errorData.message || 'Failed to save category.'}</div>`;
                }
            } catch (error) {
                console.error('Error saving category:', error);
                messageDiv.innerHTML = '<div class="error-message">Failed to save category.</div>';
            }
        });
    
        // Edit category
        function editCategory(id, restaurantId) {
            console.log(`Fetching category with ID: ${id}`);
    
            fetch(`http://localhost:5786/api/categories/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                console.log(`Response status: ${response.status}`);
                if (!response.ok) {
                    return response.json().then(errorData => {
                        throw new Error(`Error fetching category: ${errorData.message || response.statusText}`);
                    });
                }
                return response.json();
            })
            .then(category => {
                console.log('Category data:', category);
                if (category && category.name) {
                    // Populate the form fields with the fetched category data
                    categoryIdInput.value = id; 
                    categoryName.value = category.name;
                    restaurantSelect.value = restaurantId;
                    isUpdate = true;
                    modalTitle.textContent = 'Update Category';
                    categoryModal.style.display = 'flex';
                } else {
                    messageDiv.innerHTML = '<div class="error-message">Category not found.</div>';
                }
            })
            .catch(error => {
                console.error('Error loading category:', error);
                messageDiv.innerHTML = `<div class="error-message">Failed to load category: ${error.message}</div>`;
            });
        }
    
        // Delete category
        function deleteCategory(id) {
            if (confirm('Are you sure you want to delete this category?')) {
                fetch(`http://localhost:5786/api/admin/category/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                })
                .then(response => {
                    if (response.ok) {
                        messageDiv.innerHTML = '<div class="success-message">Category deleted successfully.</div>';
                        loadRestaurantsAndCategories();
                    } else {
                        return response.json().then(errorData => {
                            messageDiv.innerHTML = `<div class="error-message">${errorData.message || 'Failed to delete category.'}</div>`;
                        });
                    }
                })
                .catch(error => {
                    console.error('Error deleting category:', error);
                    messageDiv.innerHTML = '<div class="error-message">Failed to delete category.</div>';
                });
            }
        }
    
        // Initial load
        loadRestaurantsAndCategories();
    
    