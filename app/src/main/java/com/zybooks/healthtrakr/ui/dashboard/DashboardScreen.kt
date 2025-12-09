package com.zybooks.healthtrakr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    name: String,
    calorieTarget: Int,
    today: LocalDate,
    foodEntries: List<FoodEntry>,
    exerciseEntries: List<ExerciseEntry>,
    todaysSteps: Int,
    caloriesFromSteps: Int,
    onStepsChange: (Int) -> Unit,
    onAddEntryClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d")

    val todaysFood = foodEntries.filter { it.date == today }
    val todaysExercise = exerciseEntries.filter { it.date == today }

    val intakeCalories = todaysFood.sumOf { it.calories }
    val burnedFromExercise = todaysExercise.sumOf { it.caloriesBurned }
    val totalBurned = burnedFromExercise + caloriesFromSteps
    val netCalories = intakeCalories - totalBurned

    val diff = netCalories - calorieTarget
    val targetMessage = when {
        diff > 150 -> "You are above your target by ${diff} kcal."
        diff < -150 -> "You are below your target by ${-diff} kcal."
        else -> "You’re close to your target today."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLogoutClick) {
                Text("Logout")
            }
        }
        Text(
            text = "Hi, $name",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = formatter.format(today),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Daily Calories",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoColumn("Target", "$calorieTarget kcal")
                    InfoColumn("Intake", "$intakeCalories kcal")
                    InfoColumn("Burned", "$totalBurned kcal")
                    InfoColumn("Net", "$netCalories kcal")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = targetMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        StepsCard(
            todaysSteps = todaysSteps,
            caloriesFromSteps = caloriesFromSteps,
            onStepsChange = onStepsChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onAddEntryClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Entry")
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = onHistoryClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("History")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onCameraClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Food Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (todaysFood.isNotEmpty()) {
            Text("Today’s Food", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            todaysFood.forEach {
                EntryRow(
                    title = it.name,
                    subtitle = "${it.calories} kcal • P ${it.proteinGrams}g • C ${it.carbsGrams}g • F ${it.fatGrams}g"
                )
            }
        }

        if (todaysExercise.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Today’s Exercise", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            todaysExercise.forEach {
                EntryRow(
                    title = it.name,
                    subtitle = "${it.durationMinutes} min • ${it.caloriesBurned} kcal"
                )
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(value, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
private fun StepsCard(
    todaysSteps: Int,
    caloriesFromSteps: Int,
    onStepsChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Steps", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Enter steps manually",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = if (todaysSteps == 0) "" else todaysSteps.toString(),
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() }
                        onStepsChange(filtered.toIntOrNull() ?: 0)
                    },
                    label = { Text("Steps Today") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Calories from steps", style = MaterialTheme.typography.labelMedium)
                    Text("$caloriesFromSteps kcal")
                }
            }
        }
    }
}

@Composable
fun EntryRow(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(title, fontWeight = FontWeight.SemiBold)
        Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Divider(modifier = Modifier.padding(top = 6.dp))
    }
}
