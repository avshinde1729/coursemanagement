function getApiUrl(path) {
    const BASE_URL = window.location.origin;
    return `${BASE_URL}${path}`;
}

async function fetchAllTeachers() {
    try {
        const response = await fetch(getApiUrl("/teacher/all"));
        const teachers = await response.json();
        displayTeachers(teachers);
    } catch (error) {
        console.error("Error fetching teachers:", error);
    }
}

async function addTeacher() {
    const teacher = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        mobileNo: document.getElementById("mobileNo").value,
        address: document.getElementById("address").value
    };

    try {
        const response = await fetch(getApiUrl("/teacher/add"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(teacher)
        });
        if (response.ok) {
            alert("Teacher added successfully!");
            fetchAllTeachers();
        } else {
            alert("Failed to add teacher.");
        }
    } catch (error) {
        console.error("Error adding teacher:", error);
    }
}

async function updateTeacher() {
    const teacherId = document.getElementById("update-teacher-id").value;
    const updates = {
        username: document.getElementById("update-username").value,
        password: document.getElementById("update-password").value,
        firstName: document.getElementById("update-firstName").value,
        lastName: document.getElementById("update-lastName").value,
        email: document.getElementById("update-email").value,
        mobileNo: document.getElementById("update-mobileNo").value,
        address: document.getElementById("update-address").value
    };

    try {
        const response = await fetch(getApiUrl(`/teacher/update/${teacherId}`), {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updates)
        });
        if (response.ok) {
            alert("Teacher updated successfully!");
            fetchAllTeachers();
        } else {
            alert("Failed to update teacher.");
        }
    } catch (error) {
        console.error("Error updating teacher:", error);
    }
}

async function deleteTeacher() {
    const teacherId = document.getElementById("delete-teacher-id").value;

    try {
        const response = await fetch(getApiUrl(`/teacher/${teacherId}`), {
            method: "DELETE"
        });
        if (response.ok) {
            alert("Teacher deleted successfully!");
            fetchAllTeachers();
        } else {
            alert("Failed to delete teacher.");
        }
    } catch (error) {
        console.error("Error deleting teacher:", error);
    }
}

function displayTeachers(teachers) {
    const teachersList = document.getElementById("teachers-list");
    teachersList.innerHTML = teachers.map(teacher => `
        <div>
            <h3>${teacher.firstName} ${teacher.lastName}</h3>
            <p>Email: ${teacher.email}</p>
            <p>Username: ${teacher.username}</p>
            <p>Mobile: ${teacher.mobileNo}</p>
            <p>Address: ${teacher.address}</p>
        </div>
    `).join('');
}
