document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registration-form');
    const registrationMessage = document.getElementById('registration-message');

    const MOCK_REGISTER_API_ENDPOINT = 'https://mockapi.example.com/register';

    registrationForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const fullName = document.getElementById('full-name').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        registrationMessage.textContent = '';
        registrationMessage.classList.add('hidden');
        registrationMessage.classList.remove('text-red-600', 'text-green-600');

        if (password.length < 8) {
            registrationMessage.textContent = 'Password must be at least 8 characters long.';
            registrationMessage.classList.add('text-red-600', 'block');
            registrationMessage.classList.remove('hidden');
            return;
        }

        if (password !== confirmPassword) {
            registrationMessage.textContent = 'Passwords do not match.';
            registrationMessage.classList.add('text-red-600', 'block');
            registrationMessage.classList.remove('hidden');
            return;
        }

        try {
            const mockResponse = await new Promise(resolve => setTimeout(() => {
                if (email.includes('@') && email.includes('.')) {
                    resolve({
                        success: true,
                        message: 'Registration successful! Redirecting to login...'
                    });
                } else {
                    resolve({
                        success: false,
                        message: 'Invalid email format.'
                    });
                }
            }, 1000));

            if (mockResponse.success) {
                registrationMessage.textContent = mockResponse.message;
                registrationMessage.classList.add('text-green-600', 'block');
                registrationMessage.classList.remove('hidden');

                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 1500);

            } else {
                registrationMessage.textContent = mockResponse.message;
                registrationMessage.classList.add('text-red-600', 'block');
                registrationMessage.classList.remove('hidden');
            }

        } catch (error) {
            console.error('Registration error:', error);
            registrationMessage.textContent = 'An unexpected error occurred. Please try again.';
            registrationMessage.classList.add('text-red-600', 'block');
            registrationMessage.classList.remove('hidden');
        }
    });
});
