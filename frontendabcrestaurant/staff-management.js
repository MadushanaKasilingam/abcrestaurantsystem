
const staffList = document.getElementById('staffList');
const staffModal = document.getElementById('staffModal');
const modalTitle = document.getElementById('modalTitle');
const saveStaffBtn = document.getElementById('saveStaffBtn');
const addStaffBtn = document.getElementById('addStaffBtn');
const messageDiv = document.getElementById('message');
const token = localStorage.getItem('jwt');

const staffUsername = document.getElementById('staffUsername');
const staffEmail = document.getElementById('staffEmail');
const staffPassword = document.getElementById('staffPassword');

let isUpdate = false;
let currentStaffId = null;

// Fetch and display all staff members
async function loadStaffs() {
    try {
        const response = await fetch('http://localhost:5786/auth/getAllStaffs', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (response.ok) {
            const staffs = await response.json();
            staffList.innerHTML = '';
            staffs.forEach(staff => {
                const staffItem = document.createElement('div');
                staffItem.classList.add('staff-item');
                staffItem.innerHTML = `
                    <h3>${staff.username} <br> ${staff.email}</h3>

                    <div class="staff-actions">
                        <button class="btn" onclick="editStaff(${staff.id})">Edit</button>
                        <button class="btn btn-delete" onclick="deleteStaff(${staff.id})">Delete</button>
                    </div>
                `;
                staffList.appendChild(staffItem);
            });
        } else {
            staffList.innerHTML = '<p>No staff members found.</p>';
        }
    } catch (error) {
        console.error('Error loading staff:', error);
        staffList.innerHTML = '<p>Failed to load staff members.</p>';
    }
}

// Open modal to add new staff
addStaffBtn.addEventListener('click', () => {
    isUpdate = false;
    currentStaffId = null;
    staffUsername.value = '';
    staffEmail.value = '';
    staffPassword.value = '';
    modalTitle.textContent = 'Add Staff';
    staffModal.style.display = 'flex';
});

// Save staff (Add/Update)
saveStaffBtn.addEventListener('click', async () => {
    const username = staffUsername.value;
    const email = staffEmail.value;
    const password = staffPassword.value;

    if (!username || !email || !password) {
        messageDiv.innerHTML = '<div class="error-message">All fields are required.</div>';
        return;
    }

    try {
        const method = isUpdate ? 'PUT' : 'POST';
        const url = isUpdate ? `http://localhost:5786/auth/updateStaff/${currentStaffId}` : 'http://localhost:5786/auth/addStaff';
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ username, email, password })
        });

        if (response.ok) {
            messageDiv.innerHTML = '<div class="success-message">Staff saved successfully.</div>';
            loadStaffs();
            staffModal.style.display = 'none';
        } else {
            messageDiv.innerHTML = '<div class="error-message">Failed to save staff.</div>';
        }
    } catch (error) {
        console.error('Error saving staff:', error);
        messageDiv.innerHTML = '<div class="error-message">Failed to save staff.</div>';
    }
});

// Edit staff
function editStaff(id) {
    fetch(`http://localhost:5786/auth/getStaffById/${id}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(staff => {
        staffUsername.value = staff.username;
        staffEmail.value = staff.email;
        staffPassword.value = '';
        isUpdate = true;
        currentStaffId = staff.id;
        modalTitle.textContent = 'Update Staff';
        staffModal.style.display = 'flex';
    });
}

// Delete staff
function deleteStaff(id) {
    fetch(`http://localhost:5786/auth/deleteStaff/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            messageDiv.innerHTML = '<div class="success-message">Staff deleted successfully.</div>';
            loadStaffs();
        } else {
            messageDiv.innerHTML = '<div class="error-message">Failed to delete staff.</div>';
        }
    })
    .catch(error => {
        console.error('Error deleting staff:', error);
        messageDiv.innerHTML = '<div class="error-message">Failed to delete staff.</div>';
    });
}

// Close modal on click outside
window.addEventListener('click', (event) => {
    if (event.target === staffModal) {
        staffModal.style.display = 'none';
    }
});

// Load staff members on page load
loadStaffs();
