document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signupForm');
    const usernameInput = document.getElementById('usernameInput');
    const emailInput = document.getElementById('emailInput');
    const passwordInput = document.getElementById('passwordInput');
    const phoneInput = document.getElementById('phoneInput');
    const messageDiv = document.getElementById('message');

    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const userData = {
            userName: usernameInput.value,
            email: emailInput.value,
            passwordHash: passwordInput.value,
            phoneNumber: phoneInput.value,
        };
        
        messageDiv.innerHTML = '';
        messageDiv.className = '';

        try {
            const response = await axios.post('/api/auth/signup', userData);

            console.log('Signup successful:', response.data);
            messageDiv.className = 'alert alert-success';
            messageDiv.textContent = 'Signup successful! Redirecting to login...';

            setTimeout(() => {
                window.location.href = '/index.html';
            }, 2000);
            
        } catch (error) {
            console.error('Signup failed:', error.response?.data || error.message);
            messageDiv.className = 'alert alert-danger';
            messageDiv.textContent = 'Signup failed. Please try again.';
        }
    });
});