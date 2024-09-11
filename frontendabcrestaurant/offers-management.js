
const restaurantList = document.getElementById('restaurantList');
const offerModal = document.getElementById('offerModal');
const modalTitle = document.getElementById('modalTitle');
const saveOfferBtn = document.getElementById('saveOfferBtn');
const addOfferBtn = document.getElementById('addOfferBtn');
const messageDiv = document.getElementById('message');
const token = localStorage.getItem('jwt');

const offerTitle = document.getElementById('offerTitle');
const offerDescription = document.getElementById('offerDescription');
const offerDiscount = document.getElementById('offerDiscount');
const offerStartDate = document.getElementById('offerStartDate');
const offerEndDate = document.getElementById('offerEndDate');
const restaurantSelect = document.getElementById('restaurantSelect');
const offerIdInput = document.getElementById('offerId');

let isUpdate = false;
let currentOfferId = null;

// Fetch and display all restaurants and their offers
async function loadRestaurantsAndOffers() {
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

                // Fetch offers for each restaurant
                const offerResponse = await fetch(`http://localhost:5786/api/admin/offers/restaurant/${restaurant.id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (offerResponse.ok) {
                    const offers = await offerResponse.json();
                    if (offers.offers.length > 0) {
                        offers.offers.forEach(offer => {
                            const offerItem = document.createElement('div');
                            offerItem.classList.add('offer-item');
                            offerItem.innerHTML = `
                                <span>Title : ${offer.title} <br> <br> DiscountPercentage : ${offer.discountPercentage}% <br> <br> Description : ${offer.description} <br> <br> Offer start date : ${offer.startDate} <br> <br> Offer end date : ${offer.endDate}</span>
                                <div class="offer-actions">
                                    <button class="btn" onclick="editOffer(${offer.id}, ${restaurant.id})">Edit</button>
                                    <button class="btn btn-delete" onclick="deleteOffer(${offer.id})">Delete</button>
                                </div>
                            `;
                            restaurantItem.appendChild(offerItem);
                        });
                    } else {
                        const noOfferItem = document.createElement('div');
                        noOfferItem.classList.add('offer-item');
                        noOfferItem.innerHTML = `<span>No offers found.</span>`;
                        restaurantItem.appendChild(noOfferItem);
                    }
                } else {
                    const noOfferItem = document.createElement('div');
                    noOfferItem.classList.add('offer-item');
                    noOfferItem.innerHTML = `<span>Failed to load offers.</span>`;
                    restaurantItem.appendChild(noOfferItem);
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
        console.error('Error loading restaurants and offers:', error);
        restaurantList.innerHTML = '<p>Failed to load restaurants.</p>';
    }
}

// Open modal to add new offer
addOfferBtn.addEventListener('click', () => {
    isUpdate = false;
    offerTitle.value = '';
    offerDescription.value = '';
    offerDiscount.value = '';
    offerStartDate.value = '';
    offerEndDate.value = '';
    restaurantSelect.value = '';
    offerIdInput.value = '';
    modalTitle.textContent = 'Add Offer';
    offerModal.style.display = 'flex';
});

// Close modal when clicked outside of it
window.addEventListener('click', (event) => {
    if (event.target === offerModal) {
        offerModal.style.display = 'none';
    }
});

// Edit offer
async function editOffer(offerId, restaurantId) {
    try {
        const response = await fetch(`http://localhost:5786/api/admin/offers/${offerId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (response.ok) {
            const offer = await response.json();
            offerTitle.value = offer.title;
            offerDescription.value = offer.description;
            offerDiscount.value = offer.discountPercentage;
            offerStartDate.value = new Date(offer.startDate).toISOString().slice(0, 16);
            offerEndDate.value = new Date(offer.endDate).toISOString().slice(0, 16);
            restaurantSelect.value = restaurantId;
            offerIdInput.value = offer.id;
            modalTitle.textContent = 'Edit Offer';
            offerModal.style.display = 'flex';
            isUpdate = true;
            currentOfferId = offerId;
        } else {
            console.error('Failed to fetch offer details:', response.statusText);
        }
    } catch (error) {
        console.error('Error fetching offer details:', error);
    }
}

// Save offer (add/update)
saveOfferBtn.addEventListener('click', async () => {
const title = offerTitle.value;
const description = offerDescription.value;
const discountPercentage = offerDiscount.value;
const startDate = offerStartDate.value;
const endDate = offerEndDate.value;
const restaurantId = restaurantSelect.value;
const offerId = offerIdInput.value;

if (!restaurantId) {
showMessage('Please select a restaurant.', 'error');
return;
}

const offerData = {
title,
description,
discountPercentage,
startDate,
endDate
};

try {
const response = await fetch(isUpdate ? 
    `http://localhost:5786/api/admin/offers/${offerId}` : 
    `http://localhost:5786/api/admin/offers/restaurant/${restaurantId}`, {
    method: isUpdate ? 'PUT' : 'POST',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(offerData)
});

if (response.ok) {
    offerModal.style.display = 'none';
    loadRestaurantsAndOffers();
    showMessage(isUpdate ? 'Offer updated successfully!' : 'Offer added successfully!', 'success');
} else {
    const errorData = await response.json();
    console.error('Failed to save offer:', errorData.message || response.statusText);
    showMessage(`Failed to save offer: ${errorData.message || 'Please try again.'}`, 'error');
}
} catch (error) {
console.error('Error saving offer:', error);
showMessage('Error saving offer. Please try again.', 'error');
}
});

// Delete offer
async function deleteOffer(offerId) {
    if (confirm('Are you sure you want to delete this offer?')) {
        try {
            const response = await fetch(`http://localhost:5786/api/admin/offers/${offerId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                loadRestaurantsAndOffers();
                showMessage('Offer deleted successfully!', 'success');
            } else {
                console.error('Failed to delete offer:', response.statusText);
                showMessage('Failed to delete offer. Please try again.', 'error');
            }
        } catch (error) {
            console.error('Error deleting offer:', error);
            showMessage('Error deleting offer. Please try again.', 'error');
        }
    }
}

// Show success or error message
function showMessage(message, type) {
    messageDiv.textContent = message;
    messageDiv.className = type === 'success' ? 'success-message' : 'error-message';
    setTimeout(() => {
        messageDiv.textContent = '';
        messageDiv.className = '';
    }, 3000);
}

// Initial load
loadRestaurantsAndOffers();
