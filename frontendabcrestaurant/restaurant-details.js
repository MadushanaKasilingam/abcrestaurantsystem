
document.addEventListener('DOMContentLoaded', function () {
    const token = "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MjUzNTgwMjQsImV4cCI6MTc1Njg5NDAyNCwidXNlcm5hbWUiOiJtYWR1ayIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiJ9.InyECnUUBrkGUOMqyst_LSvvQNYgF3jHbe_2eNk78PCUw4Urrzgm-kVRvM1vZgtA";

    fetch('http://localhost:5786/api/restaurants', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch restaurant data. Please check your authentication.');
            }
            return response.json();
        })
        .then(data => {
            const restaurantsContainer = document.getElementById('restaurants');
            data.forEach(restaurant => {
                const restaurantDiv = document.createElement('div');
                restaurantDiv.className = 'restaurant';

                // Create image element
               // const image = document.createElement('img');
                //image.src = restaurant.images ? restaurant.images[0] : 'https://via.placeholder.com/400x300';
                //image.alt = restaurant.name;
                //restaurantDiv.appendChild(image);

                // Add restaurant name
                const name = document.createElement('h2');
                name.textContent = restaurant.name;
                restaurantDiv.appendChild(name);

                // Add description
                const description = document.createElement('p');
                description.textContent = `Description: ${restaurant.description}`;
                restaurantDiv.appendChild(description);

                // Add cuisine type
                const cuisineType = document.createElement('p');
                cuisineType.textContent = `Cuisine Type: ${restaurant.cuisineType}`;
                restaurantDiv.appendChild(cuisineType);

                // Add address
                const address = document.createElement('div');
                address.className = 'address';
                address.innerHTML = `
                   
                    <span>Street Address: ${restaurant.address.streetAddress}</span>
                    <span>City: ${restaurant.address.city}</span>
                    
                `;
                restaurantDiv.appendChild(address);

                // Add contact information
                const contactInfo = document.createElement('div');
                contactInfo.className = 'contact-info';
                contactInfo.innerHTML = `
                    <span>Mobile: ${restaurant.contactInformation.mobile}</span>
                    <span>Email: <a href="mailto:${restaurant.contactInformation.email}">${restaurant.contactInformation.email}</a></span>
                    <span>Twitter: <a href="${restaurant.contactInformation.twitter}" target="_blank">${restaurant.contactInformation.twitter}</a></span>
                    <span>Instagram: <a href="${restaurant.contactInformation.instagram}" target="_blank">${restaurant.contactInformation.instagram}</a></span>
                `;
                restaurantDiv.appendChild(contactInfo);

                // Add opening hours
                const openingHours = document.createElement('p');
                openingHours.textContent = `Opening Hours: ${restaurant.openingHours}`;
                restaurantDiv.appendChild(openingHours);



                // Add open status
                const openStatus = document.createElement('p');
                openStatus.className = 'open-status';
                openStatus.textContent = restaurant.open ? 'Open Now' : 'Closed';
                if (restaurant.open) {
                    openStatus.classList.add('open');
                }
                restaurantDiv.appendChild(openStatus);

                // Add buttons
                const buttonsDiv = document.createElement('div');
                buttonsDiv.className = 'buttons';
                buttonsDiv.innerHTML = `
                    <button class="menu-btn">Foods with prices</button>
                    <button class="offers-btn">Offers</button>
                    <button class="facilities-btn">Facilities</button>
                    <button class="gallery-btn">Gallery</button>
                `;
                restaurantDiv.appendChild(buttonsDiv);

                // Add details section
                const detailsDiv = document.createElement('div');
                detailsDiv.className = 'details';
                restaurantDiv.appendChild(detailsDiv);

                // Event listeners for buttons
                buttonsDiv.querySelector('.menu-btn').addEventListener('click', function () {
                    detailsDiv.style.display = 'block';
                    detailsDiv.textContent = `Loading menu for ${restaurant.name}...`;
                    fetch(`http://localhost:5786/api/food/restaurant/${restaurant.id}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (Array.isArray(data)) {
                                detailsDiv.innerHTML = data.map(item => `<p>${item.name} : <br> ${item.description}  <br> ${item.price+'rs'}</p>`).join('');
                            } else {
                                detailsDiv.textContent = `Unexpected response format for menu: ${JSON.stringify(data)}`;
                            }
                        })
                        .catch(error => {
                            detailsDiv.textContent = `Failed to load menu: ${error.message}`;
                        });
                });

                buttonsDiv.querySelector('.offers-btn').addEventListener('click', function () {
                    detailsDiv.style.display = 'block';
                    detailsDiv.textContent = `Loading offers for ${restaurant.name}...`;
                    fetch(`http://localhost:5786/api/admin/offers/restaurant/${restaurant.id}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.offers && Array.isArray(data.offers)) {
                                detailsDiv.innerHTML = data.offers.map(offer => `
                                    <div class="offer">
                                        <h3>${offer.title}</h3>
                                        <p>${offer.description}</p>
                                        <p>Discount: ${offer.discountPercentage}%</p>
                                        <p>Valid from ${new Date(offer.startDate).toLocaleDateString()} to ${new Date(offer.endDate).toLocaleDateString()}</p>
                                    </div>
                                `).join('');
                            } else {
                                detailsDiv.textContent = `Unexpected response format for offers: ${JSON.stringify(data)}`;
                            }
                        })
                        .catch(error => {
                            detailsDiv.textContent = `Failed to load offers: ${error.message}`;
                        });
                });


                buttonsDiv.querySelector('.facilities-btn').addEventListener('click', function () {
                    detailsDiv.style.display = 'block';
                    detailsDiv.textContent = `Loading facilities for ${restaurant.name}...`;
                    fetch(`http://localhost:5786/api/facility/restaurant/${restaurant.id}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data && Array.isArray(data.data)) {
                                detailsDiv.innerHTML = data.data.map(facility => `
                                    <div class="facility">
                                        <strong>${facility.name}</strong>: ${facility.description || 'No description available'}
                                    </div>
                                `).join('');
                            } else {
                                detailsDiv.textContent = `Unexpected response format for facilities: ${JSON.stringify(data)}`;
                            }
                        })
                        .catch(error => {
                            detailsDiv.textContent = `Failed to load facilities: ${error.message}`;
                        });
                });

                buttonsDiv.querySelector('.gallery-btn').addEventListener('click', function () {
                    detailsDiv.style.display = 'block';
                    detailsDiv.textContent = `Loading gallery for ${restaurant.name}...`;
                
                    fetch(`http://localhost:5786/api/admin/gallery/restaurant/${restaurant.id}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(response => response.json())
                    .then(data => {
                        console.log('API Response:', data);  // Log the entire response
                        if (Array.isArray(data)) {
                            if (data.length > 0) {
                                detailsDiv.innerHTML = data.map(image => {
                                    console.log('Image URL:', image.imageUrl);  // Log each image URL
                                    return `<img src="${image.imageUrl || 'https://via.placeholder.com/500'}" class="gallery-image">`;
                                }).join('');
                            } else {
                                detailsDiv.textContent = 'No images found for this restaurant.';
                            }
                        } else {
                            detailsDiv.textContent = `Unexpected response format for gallery: ${JSON.stringify(data)}`;
                        }
                    })
                    .catch(error => {
                        detailsDiv.textContent = `Failed to load gallery: ${error.message}`;
                    });
                });
                



restaurantsContainer.appendChild(restaurantDiv);

            });
        })
        .catch(error => {
            console.error('Error fetching restaurant data:', error);
        });
});

