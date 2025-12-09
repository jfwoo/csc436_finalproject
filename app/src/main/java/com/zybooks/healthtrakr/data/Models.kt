package com.zybooks.healthtrakr

import java.time.LocalDate

// Navigation destinations
enum class AppScreen {
    Onboarding1,
    Onboarding2,
    Onboarding3,
    Dashboard,
    AddEntry,
    History,
    FoodCamera
}

// User profile related
enum class ActivityLevel(val label: String, val factor: Float) {
    Sedentary("Sedentary (little or no exercise)", 1.2f),
    Light("Light (1–3 days/week)", 1.375f),
    Moderate("Moderate (3–5 days/week)", 1.55f),
    Active("Active (6–7 days/week)", 1.725f)
}

enum class Goal(val label: String, val deltaCalories: Int) {
    Lose("Lose Weight", -500),
    Maintain("Maintain Weight", 0),
    Gain("Gain Weight", 300)
}

// Tracking entries
data class FoodEntry(
    val id: Int,
    val date: LocalDate,
    val name: String,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float
)

data class ExerciseEntry(
    val id: Int,
    val date: LocalDate,
    val name: String,
    val durationMinutes: Int,
    val caloriesBurned: Int
)
