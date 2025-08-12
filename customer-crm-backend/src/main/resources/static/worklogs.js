document.addEventListener('DOMContentLoaded', () => {
    const worklogForm = document.getElementById('worklogForm');
    const worklogIdInput = document.getElementById('worklogId');
    const workNameInput = document.getElementById('workName');
    const actualAmountInput = document.getElementById('actualAmount');
    const customerCostInput = document.getElementById('customerCost');

    const worklogTableBody = document.getElementById('worklogTableBody');
    const formTitle = document.getElementById('formTitle');
    const submitButton = document.getElementById('submitButton');
    const cancelButton = document.getElementById('cancelButton');

    // WorkLogs को लोड करने का फंक्शन
    const fetchWorkLogs = async () => {
        try {
            const response = await axios.get('/api/worklogs');
            const worklogs = response.data;
            renderWorkLogs(worklogs);
        } catch (error) {
            console.error('Error fetching worklogs:', error);
            alert('Failed to load worklogs.');
        }
    };

    // WorkLogs को टेबल में दिखाने का फंक्शन
    const renderWorkLogs = (worklogs) => {
        worklogTableBody.innerHTML = '';
        worklogs.forEach(worklog => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${worklog.workName}</td>
                <td>${worklog.actualAmount.toFixed(2)}</td>
                <td>${worklog.customerCost.toFixed(2)}</td>
                <td>${new Date(worklog.createdAt).toLocaleDateString()}</td>
                <td>
                    <button class="btn btn-sm btn-info edit-btn" data-id="${worklog.id}">Edit</button>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${worklog.id}">Delete</button>
                </td>
            `;
            worklogTableBody.appendChild(row);
        });
    };

    // फॉर्म सबमिशन को संभालने का फंक्शन (Add/Edit)
    worklogForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const worklogData = {
            workName: workNameInput.value,
            actualAmount: parseFloat(actualAmountInput.value),
            customerCost: parseFloat(customerCostInput.value)
        };

        const worklogId = worklogIdInput.value;
        const method = worklogId ? 'put' : 'post';
        const url = worklogId ? `/api/worklogs/${worklogId}` : '/api/worklogs';

        try {
            await axios[method](url, worklogData);
            worklogForm.reset();
            worklogIdInput.value = '';
            formTitle.textContent = 'Add New WorkLog';
            submitButton.textContent = 'Add WorkLog';
            fetchWorkLogs();
            alert('WorkLog saved successfully!');
        } catch (error) {
            console.error('Error saving worklog:', error.response?.data || error.message);
            alert('Failed to save worklog. Please check if a worklog with the same name already exists.');
        }
    });

    // Edit बटन पर क्लिक को संभालने का फंक्शन
    worklogTableBody.addEventListener('click', (e) => {
        if (e.target.classList.contains('edit-btn')) {
            const worklogId = e.target.dataset.id;
            
            // API से पूरा WorkLog डेटा प्राप्त करें
            axios.get(`/api/worklogs/${worklogId}`).then(response => {
                const worklog = response.data;
                worklogIdInput.value = worklog.id;
                workNameInput.value = worklog.workName;
                actualAmountInput.value = worklog.actualAmount;
                customerCostInput.value = worklog.customerCost;
            });

            formTitle.textContent = 'Edit WorkLog';
            submitButton.textContent = 'Update WorkLog';
        }

        // Delete बटन पर क्लिक को संभालने का फंक्शन
        if (e.target.classList.contains('delete-btn')) {
            const worklogId = e.target.dataset.id;
            if (confirm('Are you sure you want to delete this worklog?')) {
                deleteWorkLog(worklogId);
            }
        }
    });
    
    // Delete API call
    const deleteWorkLog = async (id) => {
        try {
            await axios.delete(`/api/worklogs/${id}`);
            fetchWorkLogs();
            alert('WorkLog deleted successfully!');
        } catch (error) {
            console.error('Error deleting worklog:', error);
            alert('Failed to delete worklog.');
        }
    };

    // Cancel बटन पर क्लिक को संभालने का फंक्शन
    cancelButton.addEventListener('click', () => {
        worklogForm.reset();
        worklogIdInput.value = '';
        formTitle.textContent = 'Add New WorkLog';
        submitButton.textContent = 'Add WorkLog';
    });

    // पेज लोड होने पर WorkLogs को लोड करें
    fetchWorkLogs();
});