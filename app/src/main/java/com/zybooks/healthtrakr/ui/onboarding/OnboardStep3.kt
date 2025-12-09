package com.zybooks.healthtrakr.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.zybooks.healthtrakr.ActivityLevel
import com.zybooks.healthtrakr.Goal

@Composable
fun OnboardingStep3Screen(
    activityLevel: ActivityLevel,
    goal: Goal,
    onActivityLevelChange: (ActivityLevel) -> Unit,
    onGoalChange: (Goal) -> Unit,
    onBack: () -> Unit,
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Step 3 of 3",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text("Your Lifestyle & Goal", style = MaterialTheme.typography.titleMedium)
        Text(
            "We’ll finalize your daily calorie target.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(24.dp))

        Text("Activity Level", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        ActivityLevel.values().forEach { level ->
            SelectableRow(
                text = level.label,
                selected = level == activityLevel,
                onClick = { onActivityLevelChange(level) }
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Goal", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Goal.values().forEach { g ->
            SelectableRow(
                text = g.label,
                selected = g == goal,
                onClick = { onGoalChange(g) }
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Back")
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = onGetStarted, modifier = Modifier.weight(1f)) {
                Text("Get Started")
            }
        }
    }
}

@Composable
fun SelectableRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Text(text, modifier = Modifier.padding(12.dp))
    }
}
