document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('usernameInput');
    const passwordInput = document.getElementById('passwordInput');
    const messageDiv = document.getElementById('message');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = usernameInput.value;
        const password = passwordInput.value;
        
        messageDiv.innerHTML = '';
        messageDiv.className = '';

        try {
            // हम UI से लिए गए username को सीधे backend के userName के रूप में भेज रहे हैं
            const response = await axios.post('/api/auth/login', {
                userName: username,
                passwordHash: password,
            });

            console.log('Login successful:', response.data);
            messageDiv.className = 'alert alert-success';
            messageDiv.textContent = 'Login successful!';

            window.location.href = '/dashboard.html';
        } catch (error) {
            console.error('Login failed:', error.response?.data || error.message);
            messageDiv.className = 'alert alert-danger';
            messageDiv.textContent = 'Login failed: Invalid username or password.';
        }
    });
});