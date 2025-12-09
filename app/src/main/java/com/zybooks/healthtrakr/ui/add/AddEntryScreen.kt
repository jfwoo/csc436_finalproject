package com.zybooks.healthtrakr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddEntryScreen(
    today: LocalDate,
    onBack: () -> Unit,
    onAddFood: (String, Int, Float, Float, Float) -> Unit,
    onAddExercise: (String, Int, Int) -> Unit
) {
    var isFoodTab by rememberSaveable { mutableStateOf(true) }

    // Food state
    var foodName by rememberSaveable { mutableStateOf("") }
    var foodCalories by rememberSaveable { mutableStateOf("") }
    var protein by rememberSaveable { mutableStateOf("") }
    var carbs by rememberSaveable { mutableStateOf("") }
    var fat by rememberSaveable { mutableStateOf("") }

    // Exercise state
    var exerciseName by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var exerciseCalories by rememberSaveable { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Add Entry",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            dateFormatter.format(today),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isFoodTab = true },
                enabled = !isFoodTab
            ) { Text("Food") }
            Button(
                onClick = { isFoodTab = false },
                enabled = isFoodTab
            ) { Text("Exercise") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isFoodTab) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Add Food", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = foodCalories,
                    onValueChange = { if (it.all(Char::isDigit)) foodCalories = it },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { if (it.all { ch -> ch.isDigit() || ch == '.' }) protein = it },
                        label = { Text("Protein (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { if (it.all { ch -> ch.isDigit() || ch == '.' }) carbs = it },
                        label = { Text("Carbs (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { if (it.all { ch -> ch.isDigit() || ch == '.' }) fat = it },
                        label = { Text("Fat (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val cals = foodCalories.toIntOrNull() ?: 0
                        val p = protein.toFloatOrNull() ?: 0f
                        val c = carbs.toFloatOrNull() ?: 0f
                        val f = fat.toFloatOrNull() ?: 0f
                        onAddFood(foodName.ifBlank { "Food" }, cals, p, c, f)
                        foodName = ""
                        foodCalories = ""
                        protein = ""
                        carbs = ""
                        fat = ""
                    },
                    enabled = foodCalories.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Food")
                }
            }
        } else {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Add Exercise", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text("Exercise Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = duration,
                    onValueChange = { if (it.all(Char::isDigit)) duration = it },
                    label = { Text("Duration (minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = exerciseCalories,
                    onValueChange = { if (it.all(Char::isDigit)) exerciseCalories = it },
                    label = { Text("Calories Burned") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val mins = duration.toIntOrNull() ?: 0
                        val cals = exerciseCalories.toIntOrNull() ?: 0
                        onAddExercise(exerciseName.ifBlank { "Exercise" }, mins, cals)
                        exerciseName = ""
                        duration = ""
                        exerciseCalories = ""
                    },
                    enabled = duration.isNotBlank() && exerciseCalories.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Exercise")
                }
            }
        }
    }
}
