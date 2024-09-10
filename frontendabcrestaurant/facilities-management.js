
        const restaurantList = document.getElementById('restaurantList');
        const facilityModal = document.getElementById('facilityModal');
        const modalTitle = document.getElementById('modalTitle');
        const saveFacilityBtn = document.getElementById('saveFacilityBtn');
        const addFacilityBtn = document.getElementById('addFacilityBtn');
        const messageDiv = document.getElementById('message');
        const token = localStorage.getItem('jwt');
    
        const facilityName = document.getElementById('facilityName');
        const facilityDescription = document.getElementById('facilityDescription');
        const restaurantSelect = document.getElementById('restaurantSelect');
        const facilityIdInput = document.getElementById('facilityId');
    
        let isUpdate = false;
        let currentFacilityId = null;
    
        // Fetch and display all restaurants and their facilities
        async function loadRestaurantsAndFacilities() {
            try {
                const response = await fetch('http://localhost:5786/api/facility', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const facilities = await response.json();
                    restaurantList.innerHTML = '';
                    restaurantSelect.innerHTML = '<option value="">Select Restaurant</option>';
    
                    const restaurantMap = new Map();
                    facilities.data.forEach(facility => {
                        if (!restaurantMap.has(facility.restaurant.id)) {
                            restaurantMap.set(facility.restaurant.id, {
                                name: facility.restaurant.name,
                                facilities: []
                            });
                        }
                        restaurantMap.get(facility.restaurant.id).facilities.push(facility);
                    });
    
                    restaurantMap.forEach((value, restaurantId) => {
                        const restaurantItem = document.createElement('div');
                        restaurantItem.classList.add('restaurant-item');
                        restaurantItem.innerHTML = `<h3>${value.name}</h3>`;
    
                        value.facilities.forEach(facility => {
                            const facilityItem = document.createElement('div');
                            facilityItem.classList.add('facility-item');
                            facilityItem.innerHTML = `
                                <span>${facility.name} <br> ${facility.description}</span>
                                <div class="facility-actions">
                                    <button class="btn" onclick="editFacility(${facility.id})">Edit</button>
                                    <button class="btn btn-delete" onclick="deleteFacility(${facility.id})">Delete</button>
                                </div>
                            `;
                            restaurantItem.appendChild(facilityItem);
                        });
    
                        restaurantList.appendChild(restaurantItem);
    
                        // Add restaurant to the select dropdown
                        const option = document.createElement('option');
                        option.value = restaurantId;
                        option.textContent = value.name;
                        restaurantSelect.appendChild(option);
                    });
                } else {
                    restaurantList.innerHTML = '<p>No facilities found.</p>';
                }
            } catch (error) {
                console.error('Error loading restaurants and facilities:', error);
                restaurantList.innerHTML = '<p>Failed to load facilities.</p>';
            }
        }
    
        // Open modal to add new facility
        addFacilityBtn.addEventListener('click', () => {
            isUpdate = false;
            facilityName.value = '';
            facilityDescription.value = '';
            restaurantSelect.value = '';
            facilityIdInput.value = '';
            modalTitle.textContent = 'Add Facility';
            facilityModal.style.display = 'flex';
        });
    
        // Close modal when clicked outside of it
        window.addEventListener('click', (event) => {
            if (event.target === facilityModal) {
                facilityModal.style.display = 'none';
            }
        });
    
        // Edit facility
        async function editFacility(facilityId) {
            try {
                const response = await fetch(`http://localhost:5786/api/admin/facility/update/${facilityId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    const facility = await response.json();
                    facilityName.value = facility.name;
                    facilityDescription.value = facility.description;
                    restaurantSelect.value = facility.restaurant.id;
                    facilityIdInput.value = facility.id;
                    modalTitle.textContent = 'Edit Facility';
                    facilityModal.style.display = 'flex';
                    isUpdate = true;
                    currentFacilityId = facilityId;
                } else {
                    console.error('Failed to fetch facility details:', response.statusText);
                }
            } catch (error) {
                console.error('Error fetching facility details:', error);
            }
        }
    
        // Save facility (create/update)
        saveFacilityBtn.addEventListener('click', async () => {
            const facilityData = {
                restaurantId: restaurantSelect.value,
                name: facilityName.value,
                description: facilityDescription.value
            };
    
            try {
                const url = isUpdate
                    ? `http://localhost:5786/api/admin/facility/update/${facilityIdInput.value}`
                    : 'http://localhost:5786/api/admin/facility/create';
                const method = isUpdate ? 'PUT' : 'POST';
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(facilityData)
                });
                if (response.ok) {
                    messageDiv.innerHTML = '<p class="success-message">Facility saved successfully.</p>';
                    loadRestaurantsAndFacilities();
                    facilityModal.style.display = 'none';
                } else {
                    messageDiv.innerHTML = '<p class="error-message">Failed to save facility.</p>';
                }
            } catch (error) {
                console.error('Error saving facility:', error);
                messageDiv.innerHTML = '<p class="error-message">Error saving facility.</p>';
            }
        });
    
        // Delete facility
        async function deleteFacility(facilityId) {
            try {
                const response = await fetch(`http://localhost:5786/api/admin/facility/delete/${facilityId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.ok) {
                    messageDiv.innerHTML = '<p class="success-message">Facility deleted successfully.</p>';
                    loadRestaurantsAndFacilities();
                } else {
                    messageDiv.innerHTML = '<p class="error-message">Failed to delete facility.</p>';
                }
            } catch (error) {
                console.error('Error deleting facility:', error);
                messageDiv.innerHTML = '<p class="error-message">Error deleting facility.</p>';
            }
        }
    
        // Initial load
        loadRestaurantsAndFacilities();
    