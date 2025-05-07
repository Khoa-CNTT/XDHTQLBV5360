// Handle form submission and validation
document.addEventListener('DOMContentLoaded', function () {
    const registerForm = document.querySelector('form[th\\:action*="/register/process"]');
    if (registerForm) {
        registerForm.addEventListener('submit', function (e) {
            e.preventDefault();

            // Get form fields
            const fullName = registerForm.querySelector('input[name="fullName"]');
            const phoneNumber = registerForm.querySelector('input[name="phoneNumber"]');
            const email = registerForm.querySelector('input[name="email"]');
            const password = registerForm.querySelector('input[name="passWord"]');

            // Reset previous errors
            const notifications = document.querySelectorAll('.notification');
            notifications.forEach(notification => notification.remove());

            // Validate fields
            let hasError = false;

            if (!fullName.value.trim()) {
                showError('Vui lòng nhập họ tên');
                hasError = true;
            }

            if (!phoneNumber.value.trim()) {
                showError('Vui lòng nhập số điện thoại');
                hasError = true;
            } else if (!/^[0-9]{10}$/.test(phoneNumber.value.trim())) {
                showError('Số điện thoại không hợp lệ');
                hasError = true;
            }

            if (!email.value.trim()) {
                showError('Vui lòng nhập email');
                hasError = true;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
                showError('Email không hợp lệ');
                hasError = true;
            }

            if (!password.value) {
                showError('Vui lòng nhập mật khẩu');
                hasError = true;
            } else if (password.value.length < 6) {
                showError('Mật khẩu phải có ít nhất 6 ký tự');
                hasError = true;
            }

            if (!hasError) {
                registerForm.submit();
            }
        });
    }
});

// Function to show error notification
function showError(message) {
    const notification = document.createElement('div');
    notification.className = 'notification notification--error';
    notification.innerHTML = `
        <i class='bx bx-error-circle notification__icon'></i>
        <div class="notification__content">
            <p class="notification__message">${message}</p>
        </div>
    `;
    document.body.appendChild(notification);

    // Remove notification after animation
    setTimeout(() => {
        notification.remove();
    }, 5000);
} 