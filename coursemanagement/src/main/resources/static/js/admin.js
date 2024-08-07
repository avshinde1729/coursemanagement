
document.addEventListener('DOMContentLoaded', () => {
    const addAdminForm = document.getElementById('addAdminForm');
    const updateAdminForm = document.getElementById('updateAdminForm');
    const adminList = document.getElementById('adminList');
    const loadAdminsButton = document.getElementById('loadAdmins');
    const enrollForm = document.getElementById('enrollForm');
    const unenrollForm = document.getElementById('unenrollForm');

    addAdminForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/admin/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const admin = await response.json();
                alert('Admin added successfully!');
                addAdminForm.reset();
                loadAdmins(); // Reload the admin list
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.errors.join(', ')}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to add admin.');
        }
    });

    updateAdminForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const adminId = document.getElementById('adminIdUpdate').value;
        const username = document.getElementById('usernameUpdate').value;
        const password = document.getElementById('passwordUpdate').value;

        const adminData = {};
        if (username) adminData.username = username;
        if (password) adminData.password = password;

        try {
            const response = await fetch(`/admin/update/${adminId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(adminData)
            });

            if (response.ok) {
                const updatedAdmin = await response.json();
                alert('Admin updated successfully!');
                updateAdminForm.reset();
                loadAdmins(); // Reload the admin list
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.errors.join(', ')}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to update admin.');
        }
    });

    loadAdminsButton.addEventListener('click', loadAdmins);

    async function loadAdmins() {
        try {
            const response = await fetch('/admin/all');
            if (response.ok) {
                const admins = await response.json();
                displayAdmins(admins);
            } else {
                alert('Failed to load admins.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to load admins.');
        }
    }

    function displayAdmins(admins) {
        adminList.innerHTML = '';
        admins.forEach(admin => {
            const li = document.createElement('li');
            li.textContent = `Username: ${admin.username}`;

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.addEventListener('click', () => deleteAdmin(admin.adminId));

            li.appendChild(deleteButton);
            adminList.appendChild(li);
        });
    }

    async function deleteAdmin(adminId) {
        try {
            const response = await fetch(`/admin/delete/${adminId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Admin deleted successfully!');
                loadAdmins(); // Reload the admin list
            } else {
                alert('Failed to delete admin.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to delete admin.');
        }
    }

    enrollForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const studentId = document.getElementById('studentIdEnroll').value;
        const courseId = document.getElementById('courseIdEnroll').value;

        try {
            const response = await fetch(`/admin/student/${studentId}/courses/${courseId}/enroll`, {
                method: 'POST'
            });

            if (response.ok) {
                alert('Student enrolled in course successfully!');
                enrollForm.reset();
            } else {
                const errorText = await response.text();
                alert(`Error: ${errorText}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to enroll student.');
        }
    });

    unenrollForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const studentId = document.getElementById('studentIdUnenroll').value;
        const courseId = document.getElementById('courseIdUnenroll').value;

        try {
            const response = await fetch(`/admin/student/${studentId}/courses/${courseId}/unenroll`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Student unenrolled from course successfully!');
                unenrollForm.reset();
            } else {
                const errorText = await response.text();
                alert(`Error: ${errorText}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to unenroll student.');
        }
    });
});
