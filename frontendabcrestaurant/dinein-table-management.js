
const tableList = document.getElementById('tableList');
const tableModal = document.getElementById('tableModal');
const modalTitle = document.getElementById('modalTitle');
const saveTableBtn = document.getElementById('saveTableBtn');
const addTableBtn = document.getElementById('addTableBtn');
const messageDiv = document.getElementById('message');
const token = localStorage.getItem('jwt');

const tableNumber = document.getElementById('tableNumber');
const seats = document.getElementById('seats');
const restaurantSelect = document.getElementById('restaurantSelect');
const availableSelect = document.getElementById('available');
const tableIdInput = document.getElementById('tableId');

let isUpdate = false;
let currentTableId = null;

// Fetch and display all dine-in tables
async function loadTables() {
    try {
        const response = await fetch('http://localhost:5786/api/tables', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (response.ok) {
            const tables = await response.json();
            tableList.innerHTML = '';
            tables.forEach(table => {
                const tableItem = document.createElement('div');
                tableItem.classList.add('table-item');
                tableItem.innerHTML = `
                    <h3>Table ${table.tableNumber} (${table.seats} seats) <br> Restaurant ID: ${table.restaurantId} <br> Available: ${table.available}</h3>
                    <div class="table-actions">
                        <button class="btn" onclick="editTable(${table.id})">Edit</button>
                        <button class="btn btn-delete" onclick="deleteTable(${table.id})">Delete</button>
                    </div>
                `;
                tableList.appendChild(tableItem);
            });
        } else {
            tableList.innerHTML = '<p>No tables found.</p>';
        }
    } catch (error) {
        console.error('Error loading tables:', error);
        tableList.innerHTML = '<p>Failed to load tables.</p>';
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
        if (response.ok) {
            const restaurants = await response.json();
            restaurantSelect.innerHTML = '<option value="">Select Restaurant</option>';
            restaurants.forEach(restaurant => {
                const option = document.createElement('option');
                option.value = restaurant.id;
                option.textContent = restaurant.name;
                restaurantSelect.appendChild(option);
            });
        } else {
            console.error('Failed to load restaurants.');
        }
    } catch (error) {
        console.error('Error loading restaurants:', error);
    }
}

// Open modal to add new table
addTableBtn.addEventListener('click', () => {
    isUpdate = false;
    currentTableId = null;
    tableNumber.value = '';
    seats.value = '';
    availableSelect.value = '';
    restaurantSelect.value = '';
    modalTitle.textContent = 'Add Table';
    tableModal.style.display = 'flex';
});

// Save table (Add/Update)
saveTableBtn.addEventListener('click', async () => {
    const number = tableNumber.value;
    const seatsValue = seats.value;
    const available = availableSelect.value === 'true';
    const restaurant = restaurantSelect.value;
    const tableId = tableIdInput.value;

    if (!number || !seatsValue || !restaurant || available === '') {
        messageDiv.innerHTML = '<div class="error-message">All fields are required.</div>';
        return;
    }

    try {
        const method = isUpdate ? 'PUT' : 'POST';
        const url = isUpdate ? `http://localhost:5786/api/tables/${tableId}` : 'http://localhost:5786/api/tables';
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ tableNumber: number, seats: seatsValue, available: available, restaurantId: restaurant })
        });

        if (response.ok) {
            messageDiv.innerHTML = '<div class="success-message">Table saved successfully.</div>';
            tableModal.style.display = 'none';
            loadTables();
        } else {
            messageDiv.innerHTML = '<div class="error-message">Failed to save table.</div>';
        }
    } catch (error) {
        console.error('Error saving table:', error);
        messageDiv.innerHTML = '<div class="error-message">Failed to save table.</div>';
    }
});

// Edit table
function editTable(id) {
    fetch(`http://localhost:5786/api/tables/${id}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(table => {
        isUpdate = true;
        currentTableId = table.id;
        tableNumber.value = table.tableNumber;
        seats.value = table.seats;
        availableSelect.value = table.available ? 'true' : 'false';
        restaurantSelect.value = table.restaurantId;
        tableIdInput.value = table.id;
        modalTitle.textContent = 'Update Table';
        tableModal.style.display = 'flex';
    })
    .catch(error => {
        console.error('Error fetching table details:', error);
    });
}

// Delete table
async function deleteTable(id) {
    if (!confirm('Are you sure you want to delete this table?')) return;

    try {
        const response = await fetch(`http://localhost:5786/api/tables/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            messageDiv.innerHTML = '<div class="success-message">Table deleted successfully.</div>';
            loadTables();
        } else {
            messageDiv.innerHTML = '<div class="error-message">Failed to delete table.</div>';
        }
    } catch (error) {
        console.error('Error deleting table:', error);
        messageDiv.innerHTML = '<div class="error-message">Failed to delete table.</div>';
    }
}

// Close modal when clicking outside
window.addEventListener('click', (event) => {
    if (event.target === tableModal) {
        tableModal.style.display = 'none';
    }
});

// Initialize
loadRestaurants();
loadTables();
