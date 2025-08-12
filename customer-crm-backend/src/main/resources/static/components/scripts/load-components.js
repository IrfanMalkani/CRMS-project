async function loadComponent(elementId, filePath) {
    try {
        const response = await fetch(filePath);
        if (!response.ok) {
            throw new Error(`Failed to load component: ${filePath}`);
        }
        const html = await response.text();
        document.getElementById(elementId).innerHTML = html;
    } catch (error) {
        console.error('Error loading component:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // customers.html से components फ़ोल्डर तक पहुँचने के लिए पाथ
    // (Correct path, no changes needed)
    loadComponent('sidebar-container', 'components/sidebar.html')
        .then(() => {
            const currentPath = window.location.pathname.split('/').pop();
            const sidebarLinks = document.querySelectorAll('#sidebar-container .sidebar-menu a');
            sidebarLinks.forEach(link => {
                if (link.getAttribute('href').endsWith(currentPath)) {
                    link.parentElement.classList.add('active');
                }
            });
        });
    
    // customers.html से components फ़ोल्डर तक पहुँचने के लिए पाथ
    // (Correct path, no changes needed)
    loadComponent('header-container', 'components/header.html')
        .then(() => {
            const logoutBtn = document.getElementById('logoutBtn');
            if (logoutBtn) {
                logoutBtn.addEventListener('click', async (e) => {
                    e.preventDefault();
                    try {
                        // यहाँ लॉगआउट के लिए API कॉल
                        await axios.post('/logout'); 
                        window.location.href = '/login.html'; 
                    } catch (error) {
                        console.error('Logout failed:', error);
                        alert('Logout failed. Please try again.');
                    }
                });
            }
        });

    // फ़ुटर लोड करें
    // (Correct path, no changes needed)
    loadComponent('footer-container', 'components/footer.html');
});