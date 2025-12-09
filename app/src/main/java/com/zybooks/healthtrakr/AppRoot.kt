package com.zybooks.healthtrakr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import java.time.LocalDate
import com.zybooks.healthtrakr.ui.onboarding.OnboardingStep1Screen
import com.zybooks.healthtrakr.ui.onboarding.OnboardingStep2Screen
import com.zybooks.healthtrakr.ui.onboarding.OnboardingStep3Screen


@Composable
fun AppRoot() {
    // Navigation
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Onboarding1) }

    // Onboarding / user info
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("Male") }
    var heightInches by rememberSaveable { mutableStateOf("") }
    var weightLbs by rememberSaveable { mutableStateOf("") }
    var activityLevel by rememberSaveable { mutableStateOf(ActivityLevel.Moderate) }
    var goal by rememberSaveable { mutableStateOf(Goal.Maintain) }
    var calorieTarget by rememberSaveable { mutableIntStateOf(2000) }

    // Entries
    val foodEntries = remember { mutableStateListOf<FoodEntry>() }
    val exerciseEntries = remember { mutableStateListOf<ExerciseEntry>() }
    var nextFoodId by rememberSaveable { mutableIntStateOf(1) }
    var nextExerciseId by rememberSaveable { mutableIntStateOf(1) }

    // History & today
    val today = LocalDate.now()
    var selectedHistoryDate by rememberSaveable { mutableStateOf(today) }

    // Steps (placeholder)
    var todaysSteps by rememberSaveable { mutableIntStateOf(0) }
    var caloriesFromSteps by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentScreen) {
            AppScreen.Onboarding1 -> OnboardingStep1Screen(
                name = name,
                age = age,
                gender = gender,
                onNameChange = { name = it },
                onAgeChange = { age = it },
                onGenderChange = { gender = it },
                onNext = { currentScreen = AppScreen.Onboarding2 }
            )

            AppScreen.Onboarding2 -> OnboardingStep2Screen(
                heightInches = heightInches,
                weightLbs = weightLbs,
                onHeightChange = { heightInches = it },
                onWeightChange = { weightLbs = it },
                onBack = { currentScreen = AppScreen.Onboarding1 },
                onNext = { currentScreen = AppScreen.Onboarding3 }
            )

            AppScreen.Onboarding3 -> OnboardingStep3Screen(
                activityLevel = activityLevel,
                goal = goal,
                onActivityLevelChange = { activityLevel = it },
                onGoalChange = { goal = it },
                onBack = { currentScreen = AppScreen.Onboarding2 },
                onGetStarted = {
                    val weight = weightLbs.toFloatOrNull() ?: 150f
                    val base = (weight * 14f * activityLevel.factor).toInt()
                    calorieTarget = base + goal.deltaCalories
                    currentScreen = AppScreen.Dashboard
                }
            )

            AppScreen.Dashboard -> DashboardScreen(
                name = if (name.isBlank()) "Friend" else name,
                calorieTarget = calorieTarget,
                today = today,
                foodEntries = foodEntries,
                exerciseEntries = exerciseEntries,
                todaysSteps = todaysSteps,
                caloriesFromSteps = caloriesFromSteps,
                onStepsChange = { steps ->
                    todaysSteps = steps
                    caloriesFromSteps = (steps * 0.04f).toInt()
                },
                onAddEntryClick = { currentScreen = AppScreen.AddEntry },
                onHistoryClick = {
                    selectedHistoryDate = today
                    currentScreen = AppScreen.History
                },
                onLogoutClick = {
                    // Reset any user data if needed
                    name = ""
                    age = ""
                    gender = ""
                    heightInches = ""
                    weightLbs = ""
                    todaysSteps = 0
                    caloriesFromSteps = 0

                    // Go back to the first onboarding screen
                    currentScreen = AppScreen.Onboarding1
                },
                onCameraClick = { currentScreen = AppScreen.FoodCamera }


            )

            AppScreen.AddEntry -> AddEntryScreen(
                today = today,
                onBack = { currentScreen = AppScreen.Dashboard },
                onAddFood = { foodName, cals, protein, carbs, fat ->
                    foodEntries.add(
                        FoodEntry(
                            id = nextFoodId++,
                            date = today,
                            name = foodName,
                            calories = cals,
                            proteinGrams = protein,
                            carbsGrams = carbs,
                            fatGrams = fat
                        )
                    )
                },
                onAddExercise = { exerciseName, duration, burned ->
                    exerciseEntries.add(
                        ExerciseEntry(
                            id = nextExerciseId++,
                            date = today,
                            name = exerciseName,
                            durationMinutes = duration,
                            caloriesBurned = burned
                        )
                    )
                }
            )

            AppScreen.History -> HistoryScreen(
                selectedDate = selectedHistoryDate,
                onSelectedDateChange = { selectedHistoryDate = it },
                foodEntries = foodEntries,
                exerciseEntries = exerciseEntries,
                onBack = { currentScreen = AppScreen.Dashboard }
            )

            AppScreen.FoodCamera -> FoodCameraScreen(
                onBack = { currentScreen = AppScreen.Dashboard }
            )
        }
    }
}
