package com.zybooks.healthtrakr

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> Unit,
    foodEntries: List<FoodEntry>,
    exerciseEntries: List<ExerciseEntry>,
    onBack: () -> Unit
) {
    val allDates = (foodEntries.map { it.date } + exerciseEntries.map { it.date })
        .distinct()
        .sortedDescending()

    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    val foodsForDate = foodEntries.filter { it.date == selectedDate }
    val exercisesForDate = exerciseEntries.filter { it.date == selectedDate }

    val intakeCalories = foodsForDate.sumOf { it.calories }
    val burnedCalories = exercisesForDate.sumOf { it.caloriesBurned }

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
                "History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (allDates.isEmpty()) {
            Text(
                "No history yet. Add some food or exercise entries first.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return
        }

        Text("Select a day", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            allDates.forEach { date ->
                val isSelected = date == selectedDate
                OutlinedButton(
                    onClick = { onSelectedDateChange(date) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = formatter.format(date),
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Summary",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Intake: $intakeCalories kcal • Burned: $burnedCalories kcal • Net: ${intakeCalories - burnedCalories} kcal",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (foodsForDate.isNotEmpty()) {
                item {
                    Text("Food Entries", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(foodsForDate) { entry ->
                    EntryRow(
                        title = entry.name,
                        subtitle = "${entry.calories} kcal • P ${entry.proteinGrams}g • C ${entry.carbsGrams}g • F ${entry.fatGrams}g"
                    )
                }
            }

            if (exercisesForDate.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Exercise Entries", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(exercisesForDate) { entry ->
                    EntryRow(
                        title = entry.name,
                        subtitle = "${entry.durationMinutes} min • ${entry.caloriesBurned} kcal"
                    )
                }
            }
        }
    }
}
