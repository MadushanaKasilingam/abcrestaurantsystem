
const reservationList = document.getElementById('reservationList');
const token = localStorage.getItem('jwt');

async function loadReservations() {
    try {
        const response = await fetch('http://localhost:5786/api/reservations', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const reservations = await response.json();
            const groupedReservations = groupByRestaurant(reservations);
            displayReservations(groupedReservations);
        } else {
            reservationList.innerHTML = '<p>No reservations found.</p>';
        }
    } catch (error) {
        console.error('Error loading reservations:', error);
        reservationList.innerHTML = '<p>Failed to load reservations.</p>';
    }
}

function groupByRestaurant(reservations) {
    return reservations.reduce((acc, reservation) => {
        const restaurantName = reservation.restaurant.name;
        if (!acc[restaurantName]) {
            acc[restaurantName] = [];
        }
        acc[restaurantName].push(reservation);
        return acc;
    }, {});
}

function displayReservations(groupedReservations) {
    reservationList.innerHTML = '';

    for (const [restaurantName, reservations] of Object.entries(groupedReservations)) {
        const restaurantDiv = document.createElement('div');
        restaurantDiv.innerHTML = `<div class="restaurant-name">${restaurantName}</div>`;
        
        reservations.forEach(reservation => {
            const reservationItem = document.createElement('div');
            reservationItem.classList.add('reservation-item');
            reservationItem.innerHTML = `
                <div>
                    <h3>Table ${reservation.dineinTable.tableNumber}</h3>
                    <div class="reservation-details">
                        <p>Reserved by: ${reservation.user.username}</p>
                        <p>Reservation Time: ${new Date(reservation.reservationTime).toLocaleString()}</p>
                        <p>Number of Guests: ${reservation.numberOfGuests}</p>
                        <p>Special Requests: ${reservation.specialRequests || 'None'}</p>
                    </div>
                </div>
            `;
            restaurantDiv.appendChild(reservationItem);
        });

        reservationList.appendChild(restaurantDiv);
    }
}

// Load reservations on page load
loadReservations();
