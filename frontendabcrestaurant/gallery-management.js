
        const restaurantList = document.getElementById('restaurantList');
        const imageModal = document.getElementById('imageModal');
        const modalTitle = document.getElementById('modalTitle');
        const saveImageBtn = document.getElementById('saveImageBtn');
        const addImageBtn = document.getElementById('addImageBtn');
        const messageDiv = document.getElementById('message');
        const token = localStorage.getItem('jwt');
    
        const imageUrl = document.getElementById('imageUrl');
        const restaurantSelect = document.getElementById('restaurantSelect');
        const imageIdInput = document.getElementById('imageId');
    
        let isUpdate = false;
    
        async function loadRestaurantsAndImages() {
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
    
                        const imageResponse = await fetch(`http://localhost:5786/api/admin/gallery/restaurant/${restaurant.id}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`
                            }
                        });
                        if (imageResponse.ok) {
                            const images = await imageResponse.json();
                            if (images.length > 0) {
                                images.forEach(image => {
                                    const imageItem = document.createElement('div');
                                    imageItem.classList.add('gallery-item');
                                    imageItem.innerHTML = `
                                        <img src="${image.url}" alt="Image" style="max-width: 100px; height: auto;">
                                        <div class="gallery-actions">
                                            <button class="btn" onclick="editImage(${image.id})">Edit</button>
                                            <button class="btn btn-delete" onclick="deleteImage(${image.id})">Delete</button>
                                        </div>
                                    `;
                                    restaurantItem.appendChild(imageItem);
                                });
                            } else {
                                const noImageItem = document.createElement('div');
                                noImageItem.classList.add('gallery-item');
                                noImageItem.innerHTML = `<span>No images found.</span>`;
                                restaurantItem.appendChild(noImageItem);
                            }
                        } else {
                            const noImageItem = document.createElement('div');
                            noImageItem.classList.add('gallery-item');
                            noImageItem.innerHTML = `<span>Failed to load images.</span>`;
                            restaurantItem.appendChild(noImageItem);
                        }
    
                        restaurantList.appendChild(restaurantItem);
    
                        const option = document.createElement('option');
                        option.value = restaurant.id;
                        option.textContent = restaurant.name;
                        restaurantSelect.appendChild(option);
                    }
                } else {
                    restaurantList.innerHTML = '<p>No restaurants found.</p>';
                }
            } catch (error) {
                console.error('Error loading restaurants and images:', error);
                restaurantList.innerHTML = '<p>Failed to load restaurants.</p>';
            }
        }
    
        addImageBtn.addEventListener('click', () => {
            isUpdate = false;
            imageUrl.value = '';
            restaurantSelect.value = '';
            imageIdInput.value = '';
            modalTitle.textContent = 'Add Image';
            imageModal.style.display = 'flex';
        });
    
        saveImageBtn.addEventListener('click', async () => {
            const url = imageUrl.value;
            const restaurantId = parseInt(restaurantSelect.value, 10);
            const imageId = imageIdInput.value;
    
            if (!url || isNaN(restaurantId)) {
                messageDiv.innerHTML = '<div class="error-message">All fields are required and restaurant must be selected.</div>';
                return;
            }
    
            const method = isUpdate ? 'PUT' : 'POST';
            const apiUrl = isUpdate ? `http://localhost:5786/api/admin/gallery/${imageId}` : 'http://localhost:5786/api/admin/gallery';
    
            try {
                const response = await fetch(apiUrl, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        url: url,
                        restaurantId: restaurantId
                    })
                });
    
                if (response.ok) {
                    messageDiv.innerHTML = '<div class="success-message">Image saved successfully.</div>';
                    imageModal.style.display = 'none';
                    loadRestaurantsAndImages();
                } else {
                    const errorData = await response.json();
                    messageDiv.innerHTML = `<div class="error-message">Failed to save image</div>`;
                }
                
            } catch (error) {
                console.error('Error saving image:', error);
                messageDiv.innerHTML = '<div class="error-message">An error occurred while saving the image.</div>';
            }
        });
    
        function editImage(id) {
            isUpdate = true;
            fetch(`http://localhost:5786/api/admin/gallery/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch image details');
                }
            })
            .then(image => {
                if (image) {
                    imageUrl.value = image.url || ''; 
                    restaurantSelect.value = image.restaurantId || ''; 
                    imageIdInput.value = image.id || ''; 
                    modalTitle.textContent = 'Edit Image';
                    imageModal.style.display = 'flex';
                } else {
                    throw new Error('Image data is undefined');
                }
            })
            .catch(error => {
                console.error('Error fetching image details:', error);
                messageDiv.innerHTML = '<div class="error-message">Failed to fetch image details.</div>';
            });
        }
    
        async function deleteImage(id) {
            if (!confirm('Are you sure you want to delete this image?')) {
                return;
            }
    
            try {
                const response = await fetch(`http://localhost:5786/api/admin/gallery/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
    
                if (response.ok) {
                    messageDiv.innerHTML = '<div class="success-message">Image deleted successfully.</div>';
                    loadRestaurantsAndImages();
                } else {
                    const errorText = await response.text();
                    console.error('Failed to delete image:', errorText);
                    messageDiv.innerHTML = `<div class="error-message">Failed to delete image: ${response.statusText}</div>`;
                }
            } catch (error) {
                console.error('Error deleting image:', error);
                messageDiv.innerHTML = '<div class="error-message">An error occurred while deleting the image.</div>';
            }
        }
    
        window.addEventListener('click', (event) => {
            if (event.target === imageModal) {
                imageModal.style.display = 'none';
            }
        });
    
        loadRestaurantsAndImages();
   