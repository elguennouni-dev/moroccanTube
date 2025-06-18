document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const loginMessage = document.getElementById('login-message');

    const MOCK_API_ENDPOINT = 'https://mockapi.example.com/login';

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const rememberMe = document.getElementById('remember-me').checked;

        loginMessage.textContent = '';
        loginMessage.classList.add('hidden');

        try {
            const mockResponse = await new Promise(resolve => setTimeout(() => {
                if (email === 'user@example.com' && password === 'password123') {
                    resolve({
                        success: true,
                        token: 'mock-jwt-token-12345',
                        message: 'Login successful!'
                    });
                } else {
                    resolve({
                        success: false,
                        message: 'Invalid email or password.'
                    });
                }
            }, 1000));

            if (mockResponse.success) {
                localStorage.setItem('jwt_token', mockResponse.token);
                loginMessage.textContent = mockResponse.message;
                loginMessage.classList.remove('text-red-600');
                loginMessage.classList.add('text-green-600');
                loginMessage.classList.remove('hidden');

                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1500);

            } else {
                loginMessage.textContent = mockResponse.message;
                loginMessage.classList.remove('text-green-600');
                loginMessage.classList.add('text-red-600');
                loginMessage.classList.remove('hidden');
            }

        } catch (error) {
            console.error('Login error:', error);
            loginMessage.textContent = 'An unexpected error occurred. Please try again.';
            loginMessage.classList.remove('text-green-600');
            loginMessage.classList.add('text-red-600');
            loginMessage.classList.remove('hidden');
        }
    });
});
