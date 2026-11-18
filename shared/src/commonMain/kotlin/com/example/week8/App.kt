
package com.example.week8

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

// Task 3 - Display the student list
@Composable
fun StudentList(students: List<Student>) {
    LazyColumn {
        items(students) { student ->
            Text(student.toString())
        }
    }
}

// Task 8 - Student type dropdown
@Composable
fun DropdownList(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val dropDownVisible = remember {
        mutableStateOf(false)
    }

    Box {
        Button(
            onClick = {
                dropDownVisible.value = true
            }
        ) {
            Text(selectedItem)
        }

        DropdownMenu(
            expanded = dropDownVisible.value,
            onDismissRequest = {
                dropDownVisible.value = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        dropDownVisible.value = false
                    }
                )
            }
        }
    }
}

// Task 5 - Add student using a callback
@Composable
fun AddStudent(onStudentAdded: (Student) -> Unit) {

    val idState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }
    val courseState = remember { mutableStateOf("") }
    val markState = remember { mutableStateOf("") }
    val errorState = remember { mutableStateOf("") }

    // Task 8 - Selected student type
    val studentType = remember {
        mutableStateOf("Undergraduate")
    }

    Column {
        OutlinedTextField(
            value = idState.value,
            onValueChange = { idState.value = it },
            singleLine = true,
            label = { Text("Student ID") }
        )

        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            singleLine = true,
            label = { Text("Student name") }
        )

        OutlinedTextField(
            value = courseState.value,
            onValueChange = { courseState.value = it },
            singleLine = true,
            label = { Text("Course") }
        )

        OutlinedTextField(
            value = markState.value,
            onValueChange = { markState.value = it },
            singleLine = true,
            label = { Text("Mark") }
        )

        // Task 8 - Choose student type
        Text("Select student type")

        DropdownList(
            items = listOf("Undergraduate", "Masters"),
            selectedItem = studentType.value,
            onItemSelected = {
                studentType.value = it
            }
        )

        Button(
            onClick = {
                val mark = markState.value.toIntOrNull()

                if (idState.value.isBlank() ||
                    nameState.value.isBlank() ||
                    courseState.value.isBlank()
                ) {
                    errorState.value = "Please complete all fields"
                } else if (mark == null || mark !in 0..100) {
                    errorState.value =
                        "Enter a valid mark between 0 and 100"
                } else {

                    // Task 8 - Create the correct student type
                    val student: Student =
                        if (studentType.value == "Undergraduate") {
                            Undergraduate(
                                idState.value,
                                nameState.value,
                                courseState.value
                            )
                        } else {
                            Masters(
                                idState.value,
                                nameState.value,
                                courseState.value
                            )
                        }

                    student.mark = mark

                    onStudentAdded(student)

                    idState.value = ""
                    nameState.value = ""
                    courseState.value = ""
                    markState.value = ""
                    errorState.value = ""
                }
            }
        ) {
            Text("Add Student")
        }

        Text(errorState.value, color = Color.Red)
    }
}

// Task 7 - Search students by course
@Composable
fun SearchByCourse(onCourseEntered: (String) -> Unit) {

    val courseState = remember { mutableStateOf("") }

    Column {
        Text("Enter course")

        OutlinedTextField(
            value = courseState.value,
            onValueChange = { courseState.value = it },
            singleLine = true
        )

        Button(
            onClick = {
                onCourseEntered(courseState.value)
            }
        ) {
            Text("Search")
        }
    }
}

// Main application
@Composable
fun App() {

    // Task 6 - University object
    val university = remember {
        University()
    }

    val studentList = remember {
        mutableStateListOf<Student>()
    }

    MaterialTheme {
        Column {
            Text("University Student Management")

            AddStudent(
                onStudentAdded = { student ->

                    university.addStudent(student)

                    studentList.clear()
                    studentList.addAll(university.studentList)
                }
            )

            // Task 7 - Search by course
            SearchByCourse(
                onCourseEntered = { course ->

                    val matches =
                        university.findStudentsByCourse(course)

                    studentList.clear()
                    studentList.addAll(matches)
                }
            )

            Button(
                onClick = {
                    studentList.clear()
                    studentList.addAll(university.studentList)
                }
            ) {
                Text("Show All Students")
            }

            Text("Number of students: ${studentList.size}")

            Text(
                "Student List",
                fontWeight = FontWeight.Bold
            )

            StudentList(studentList.toList())
        }
    }
}
