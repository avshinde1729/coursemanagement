const apiUrl = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
    fetchAdmins();
    fetchStudents();
    fetchCourses();
    fetchTeachers();
});

async function fetchAdmins() {
    try {
        const response = await fetch(`${apiUrl}/admin/all`);
        const admins = await response.json();
        const list = document.getElementById('admin-list');
        list.innerHTML = '';
        admins.forEach(admin => {
            const li = document.createElement('li');
            li.textContent = `Username: ${admin.username}`;
            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.className = 'delete';
            deleteButton.onclick = () => deleteAdmin(admin.adminId);
            li.appendChild(deleteButton);
            list.appendChild(li);
        });
    } catch (error) {
        console.error('Error fetching admins:', error);
    }
}

async function addAdmin() {
    const username = document.getElementById('admin-username').value.trim();
    const password = document.getElementById('admin-password').value.trim();
    if (!username || !password) {
        alert('Please fill in all fields.');
        return;
    }

    try {
        const response = await fetch(`${apiUrl}/admin/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (response.ok) {
            fetchAdmins();
        } else {
            console.error('Error adding admin:', await response.text());
        }
    } catch (error) {
        console.error('Error adding admin:', error);
    }
}

async function deleteAdmin(id) {
    try {
        const response = await fetch(`${apiUrl}/admin/delete/${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            fetchAdmins();
        } else {
            console.error('Error deleting admin:', await response.text());
        }
    } catch (error) {
        console.error('Error deleting admin:', error);
    }
}

// Similar functions for Students, Courses, and Teachers
// Add the `fetchStudents`, `addStudent`, `deleteStudent`, etc.
// Implement them similarly to `fetchAdmins` and `addAdmin` above.





//document.addEventListener('DOMContentLoaded', () => {
//    const addAdminForm = document.getElementById('addAdminForm');
//    const adminList = document.getElementById('adminList');
//    const loadAdminsButton = document.getElementById('loadAdmins');
//
//    addAdminForm.addEventListener('submit', async (event) => {
//        event.preventDefault();
//        const username = document.getElementById('username').value;
//        const password = document.getElementById('password').value;
//
//        try {
//            const response = await fetch('/admin/add', {
//                method: 'POST',
//                headers: {
//                    'Content-Type': 'application/json'
//                },
//                body: JSON.stringify({ username, password })
//            });
//
//            if (response.ok) {
//                const admin = await response.json();
//                alert('Admin added successfully!');
//                addAdminForm.reset();
//                loadAdmins(); // Reload the admin list
//            } else {
//                const errorData = await response.json();
//                alert(`Error: ${errorData.errors.join(', ')}`);
//            }
//        } catch (error) {
//            console.error('Error:', error);
//            alert('Failed to add admin.');
//        }
//    });
//
//    loadAdminsButton.addEventListener('click', loadAdmins);
//
//    async function loadAdmins() {
//        try {
//            const response = await fetch('http://localhost:8080/admin/all');
//            if (response.ok) {
//                const admins = await response.json();
//                displayAdmins(admins);
//            } else {
//                alert('Failed to load admins.');
//            }
//        } catch (error) {
//            console.error('Error:', error);
//            alert('Failed to load admins.');
//        }
//    }
//
//    function displayAdmins(admins) {
//        adminList.innerHTML = '';
//        admins.forEach(admin => {
//            const li = document.createElement('li');
//            li.textContent = `Username: ${admin.username}`;
//
//            const deleteButton = document.createElement('button');
//            deleteButton.textContent = 'Delete';
//            deleteButton.addEventListener('click', () => deleteAdmin(admin.adminId));
//
//            li.appendChild(deleteButton);
//            adminList.appendChild(li);
//        });
//    }
//
//    async function deleteAdmin(adminId) {
//        try {
//            const response = await fetch(`/admin/delete/${adminId}`, {
//                method: 'DELETE'
//            });
//
//            if (response.ok) {
//                alert('Admin deleted successfully!');
//                loadAdmins(); // Reload the admin list
//            } else {
//                alert('Failed to delete admin.');
//            }
//        } catch (error) {
//            console.error('Error:', error);
//            alert('Failed to delete admin.');
//        }
//    }
//});
