
document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwt');
    if (!token) {
        window.location.href = 'login.html'; // Redirect to login if no token
    } else {
        loadQueries(token);
    }
});

async function loadQueries(token) {
    try {
        const response = await fetch('http://localhost:5786/api/user-queries/all', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const queries = await response.json();
            console.log('Loaded queries:', queries); // Log the queries
            displayQueries(queries);
        } else {
            const errorText = await response.text();
            console.error('API error:', response.status, errorText);
            document.getElementById('query-list').innerHTML = `
                <p>Failed to load queries. Status: ${response.status}. ${errorText}</p>
            `;
        }
    } catch (error) {
        console.error('Error loading queries:', error);
        document.getElementById('query-list').innerHTML = `
            <p>Failed to load queries. Please check the console for details.</p>
        `;
    }
}

function displayQueries(queries) {
    const queryList = document.getElementById('query-list');
    queryList.innerHTML = '';
    queries.forEach(query => {
        const queryItem = document.createElement('div');
        queryItem.className = 'query-item';
        queryItem.innerHTML = `
            <h3>Query from ${query.userId ? `User ${query.userId}` : 'Unknown User'}</h3>
            <p class="query-details"><strong>Subject:</strong> ${query.subject || 'No subject provided'}</p>
            <p class="query-details"><strong>Message:</strong> ${query.message || 'No message provided'}</p>
            <div class="response-form">
                <textarea id="response-${query.id}" placeholder="Write your response here..."></textarea>
                <button onclick="submitResponse(${query.id})">Submit Response</button>
            </div>
        `;
        queryList.appendChild(queryItem);
    });
}

async function submitResponse(queryId) {
    const token = localStorage.getItem('jwt');
    const responderId = localStorage.getItem('responderId'); // Retrieve the responder ID from local storage
    const responseText = document.getElementById(`response-${queryId}`).value;

    if (!responseText) {
        alert('Please enter a response.');
        return;
    }

    if (!responderId) {
        alert('Responder ID is not available.');
        console.log('Stored Responder ID:', responderId); // Debugging
        return;
    }

    try {
        const response = await fetch('http://localhost:5786/api/query-responses', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                queryId,
                responderId,
                response: responseText
            })
        });

        if (response.ok) {
            alert('Response submitted successfully.');
            document.getElementById(`response-${queryId}`).value = ''; // Clear the textarea
        } else {
            const errorText = await response.text();
            console.error('API error:', response.status, errorText);
            alert('Failed to submit response. Please try again later.');
        }
    } catch (error) {
        console.error('Error submitting response:', error);
        alert('Failed to submit response. Please check the console for details.');
    }
}
