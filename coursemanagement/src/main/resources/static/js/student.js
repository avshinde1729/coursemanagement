
function getApiUrl(path) {
    const BASE_URL = window.location.origin; // This will be "http://localhost:8080" in your case
    return `${BASE_URL}${path}`;
}

async function fetchAllStudents() {
    try {
        const response = await fetch(getApiUrl("/student/all"));
        const students = await response.json();
        displayStudents(students);
    } catch (error) {
        console.error("Error fetching students:", error);
    }
}

async function addStudent() {
    const student = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        mobileNo: document.getElementById("mobileNo").value,
        address: document.getElementById("address").value,
        classDivision: document.getElementById("classDivision").value
    };

    try {
        const response = await fetch(getApiUrl("/student/add"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(student)
        });
        if (response.ok) {
            alert("Student added successfully!");
            fetchAllStudents();
        } else {
            alert("Failed to add student.");
        }
    } catch (error) {
        console.error("Error adding student:", error);
    }
}

async function updateStudent() {
    const studentId = document.getElementById("update-student-id").value;
    const updates = {
        username: document.getElementById("update-username").value,
        password: document.getElementById("update-password").value,
        firstName: document.getElementById("update-firstName").value,
        lastName: document.getElementById("update-lastName").value,
        email: document.getElementById("update-email").value,
        mobileNo: document.getElementById("update-mobileNo").value,
        address: document.getElementById("update-address").value,
        classDivision: document.getElementById("update-classDivision").value
    };

    try {
        const response = await fetch(getApiUrl(`/student/${studentId}/update`), {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updates)
        });
        if (response.ok) {
            alert("Student updated successfully!");
            fetchAllStudents();
            updateStudentForm.reset();
        } else {
            const errorData = await response.json();
            alert(`Failed to update student: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error updating student:", error);
        alert("Error updating student.");
    }
}

async function deleteStudent() {
    const studentId = document.getElementById("delete-student-id").value;

    try {
        const response = await fetch(getApiUrl(`/student/${studentId}`), {
            method: "DELETE"
        });
        if (response.ok) {
            alert("Student deleted successfully!");
            fetchAllStudents();
        } else {
            alert("Failed to delete student.");
        }
    } catch (error) {
        console.error("Error deleting student:", error);
    }
}

async function fetchCoursesByStudentId() {
    const studentId = document.getElementById("student-courses-id").value;

    try {
        const response = await fetch(getApiUrl(`/student/${studentId}/courses`));
        const courses = await response.json();
        displayCourses(courses);
    } catch (error) {
        console.error("Error fetching courses:", error);
    }
}

function displayStudents(students) {
    const studentsList = document.getElementById("students-list");
    studentsList.innerHTML = students.map(student => `
        <div>
            <h3>${student.firstName} ${student.lastName}</h3>
            <p>Email: ${student.email}</p>
            <p>Username: ${student.username}</p>
            <p>Mobile: ${student.mobileNo}</p>
            <p>Address: ${student.address}</p>
            <p>Class Division: ${student.classDivision}</p>
        </div>
    `).join('');
}

function displayCourses(courses) {
    const coursesList = document.getElementById("student-courses-list");
    coursesList.innerHTML = courses.map(course => `
        <div>
            <h3>${course.courseName}</h3>
            <p>Description: ${course.description}</p>
            <p>Duration: ${course.durationInMonth} months</p>
            <p>Assigned Date: ${new Date(course.assignedDate).toLocaleDateString()}</p>
            <p>End Date: ${new Date(course.endDate).toLocaleDateString()}</p>
        </div>
    `).join('');
}
