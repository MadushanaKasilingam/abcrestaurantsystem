
// Check if user is authenticated
const token = localStorage.getItem('jwt');
if (!token) {
    window.location.href = 'login.html';
}

document.getElementById('queryForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value;

    fetch('http://localhost:5786/api/user-queries', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            subject: subject,
            message: message
        })
    })
    .then(response => response.json())
    .then(data => {
        alert('Query submitted successfully!');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to submit the query.');
    });
});
