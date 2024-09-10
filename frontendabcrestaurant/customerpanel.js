async function fetchData() {
    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            console.error('No JWT token found');
            return;
        }

        // Fetch user details
        let response = await fetch('http://localhost:5786/api/users/me', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        let user = await response.json();
        console.log('User data:', user);

        // Display user details
        const userDetailsContent = document.getElementById('user-details-content');
        userDetailsContent.innerHTML = `
            <p>User ID: ${user.id}</p>
            <p>Username: ${user.username}</p>
            <p>Password: ${user.password}</p>
            <p>Email: ${user.email}</p>
        `;

        // Fetch reservations
        response = await fetch(`http://localhost:5786/api/reservations/user/${user.id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        let reservations = await response.json();
        console.log('Reservations data:', reservations);

        // Display reservations
        const reservationsContent = document.getElementById('reservations-content');
        reservationsContent.innerHTML = reservations.map(reservation => `
            <p>Reservation ID: ${reservation.id}, Restaurant: ${reservation.restaurant}, Date: ${reservation.reservationTime}, Seats: ${reservation.numberOfGuests}</p>
        `).join('');

        // Fetch queries
        response = await fetch('/api/queries', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        let queries = await response.json();
        console.log('Queries data:', queries);

        // Display queries
        const queriesContent = document.getElementById('queries-content');
        queriesContent.innerHTML = queries.map(query => `
            <p>Query ID: ${query.id}, Content: ${query.content}, Status: ${query.status}</p>
        `).join('');

        // Fetch responses
        response = await fetch('/api/responses', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        let responses = await response.json();
        console.log('Responses data:', responses);

        // Display responses
        const responsesContent = document.getElementById('responses-content');
        responsesContent.innerHTML = responses.map(response => `
            <p>Response ID: ${response.id}, Query ID: ${response.queryId}, Content: ${response.content}, Date: ${response.responseDate}</p>
        `).join('');

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

function logout() {
    // Clear session storage or local storage if needed
    localStorage.removeItem('jwtToken');

    // Redirect to the login page
    window.location.href = 'login.html';
}

document.addEventListener('DOMContentLoaded', fetchData);
