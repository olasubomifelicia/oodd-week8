
package com.example.week8

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.layout.Column
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

// Task 5 - Add student using a callback
@Composable
fun AddStudent(onStudentAdded: (Student) -> Unit) {

    val idState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }
    val courseState = remember { mutableStateOf("") }
    val markState = remember { mutableStateOf("") }
    val errorState = remember { mutableStateOf("") }

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
                    val student = Undergraduate(
                        idState.value,
                        nameState.value,
                        courseState.value
                    )

                    student.mark = mark

                    // Send student back to App
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

// Main application
@Composable
fun App() {

    // Task 1 - Student state list
    val studentList = remember {
        mutableStateListOf<Student>()
    }

    MaterialTheme {
        Column {
            Text("University Student Management")

            // Task 5 - Receive student through callback
            AddStudent(
                onStudentAdded = {
                    studentList.add(it)
                }
            )

            Text("Number of students: ${studentList.size}")

            Text(
                "Student List",
                fontWeight = FontWeight.Bold
            )

            StudentList(studentList.toList())
        }
    }
}
