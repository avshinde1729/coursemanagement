document.addEventListener('DOMContentLoaded', () => {
    const addCourseForm = document.getElementById('addCourseForm');
    const courseList = document.getElementById('courseList');
    const loadCoursesButton = document.getElementById('loadCourses');

    addCourseForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const courseName = document.getElementById('courseName').value;
        const description = document.getElementById('description').value;
        const durationInMonth = document.getElementById('durationInMonth').value;
        const assignedDate = document.getElementById('assignedDate').value;
        const endDate = document.getElementById('endDate').value;
        const teacherId = document.getElementById('teacherId').value;

        try {
            const response = await fetch('/course/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    courseName,
                    description,
                    durationInMonth,
                    assignedDate,
                    endDate,
                    teacher: { teacherId }
                })
            });

            if (response.ok) {
                const course = await response.json();
                alert('Course added successfully!');
                addCourseForm.reset();
                loadCourses(); // Reload the course list
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.errors.join(', ')}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to add course.');
        }
    });

    loadCoursesButton.addEventListener('click', loadCourses);

    async function loadCourses() {
        try {
            const response = await fetch('/course/all');
            if (response.ok) {
                const courses = await response.json();
                displayCourses(courses);
            } else {
                alert('Failed to load courses.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to load courses.');
        }
    }

    function displayCourses(courses) {
        courseList.innerHTML = '';
        courses.forEach(course => {
            const li = document.createElement('li');
            li.textContent = `Course: ${course.courseName}`;

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.addEventListener('click', () => deleteCourse(course.courseId));

            li.appendChild(deleteButton);
            courseList.appendChild(li);
        });
    }

    async function deleteCourse(courseId) {
        try {
            const response = await fetch(`/course/${courseId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Course deleted successfully!');
                loadCourses(); // Reload the course list
            } else {
                alert('Failed to delete course.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Failed to delete course.');
        }
    }
});