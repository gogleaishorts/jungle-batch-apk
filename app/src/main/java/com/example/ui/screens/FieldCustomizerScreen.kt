package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FieldConfig
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.PetGroup1Green
import com.example.ui.theme.TacticalGold

@Composable
fun FieldCustomizerScreen(
    currentConfig: FieldConfig,
    onSaveConfig: (FieldConfig) -> Unit,
    onResetConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    var personLabel by remember(currentConfig) { mutableStateOf(currentConfig.personLabel) }
    var idLabel by remember(currentConfig) { mutableStateOf(currentConfig.idLabel) }
    var groupLabel by remember(currentConfig) { mutableStateOf(currentConfig.groupLabel) }
    var categoryLabel by remember(currentConfig) { mutableStateOf(currentConfig.categoryLabel) }

    var metricRunLabel by remember(currentConfig) { mutableStateOf(currentConfig.metricRunLabel) }
    var metricPullUpsLabel by remember(currentConfig) { mutableStateOf(currentConfig.metricPullUpsLabel) }
    var metricPushUpsLabel by remember(currentConfig) { mutableStateOf(currentConfig.metricPushUpsLabel) }
    var metricDandBaithakLabel by remember(currentConfig) { mutableStateOf(currentConfig.metricDandBaithakLabel) }
    var metricSitUpsLabel by remember(currentConfig) { mutableStateOf(currentConfig.metricSitUpsLabel) }
    var qualifier1Label by remember(currentConfig) { mutableStateOf(currentConfig.qualifier1Label) }
    var qualifier2Label by remember(currentConfig) { mutableStateOf(currentConfig.qualifier2Label) }
    var availableBatches by remember(currentConfig) { mutableStateOf(currentConfig.availableBatches) }

    var saveFeedback by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ArmyGreenDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FIELD & METRIC CUSTOMIZER",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Rename all data fields, items & drill names to match your academy",
                                style = MaterialTheme.typography.bodySmall,
                                color = TacticalGold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = TacticalGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Quick Presets
        item {
            Text(
                text = "QUICK PRESETS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = personLabel == "Cadet",
                    onClick = {
                        personLabel = "Cadet"
                        idLabel = "Roll No"
                        groupLabel = "Batch"
                        categoryLabel = "Target Exam"
                        metricRunLabel = "1600m Run Timing"
                        metricPullUpsLabel = "Pull-ups (Beam)"
                        metricPushUpsLabel = "Push-ups"
                        metricDandBaithakLabel = "Dand-Baithak (Squats)"
                        metricSitUpsLabel = "Sit-ups"
                        qualifier1Label = "9-ft Ditch Jump"
                        qualifier2Label = "Zig-zag Balance"
                        availableBatches = "Alpha Commando, Bravo Agnipath, Charlie SSC-GD, Delta NDA/CDS"
                    },
                    label = { Text("Army / Agniveer") }
                )

                FilterChip(
                    selected = personLabel == "Trainee",
                    onClick = {
                        personLabel = "Trainee"
                        idLabel = "Chest No"
                        groupLabel = "Squad"
                        categoryLabel = "Police / CAPF"
                        metricRunLabel = "5km / 1600m Run"
                        metricPullUpsLabel = "Beam (Chin-ups)"
                        metricPushUpsLabel = "Push-ups Reps"
                        metricDandBaithakLabel = "Desi Squats"
                        metricSitUpsLabel = "Sit-ups Crunches"
                        qualifier1Label = "High / Long Jump"
                        qualifier2Label = "Obstacle Course"
                        availableBatches = "Squad 1, Squad 2, Commando Platoon"
                    },
                    label = { Text("Police / CAPF") }
                )

                FilterChip(
                    selected = personLabel == "Athlete",
                    onClick = {
                        personLabel = "Athlete"
                        idLabel = "Member ID"
                        groupLabel = "Training Group"
                        categoryLabel = "Program"
                        metricRunLabel = "1 Mile Time Trial"
                        metricPullUpsLabel = "Strict Pull-ups"
                        metricPushUpsLabel = "Floor Push-ups"
                        metricDandBaithakLabel = "Air Squats"
                        metricSitUpsLabel = "Abmat Sit-ups"
                        qualifier1Label = "Broad Jump"
                        qualifier2Label = "Agility Ladder"
                        availableBatches = "Morning Fitness, Elite Endurance, Strength Group"
                    },
                    label = { Text("CrossFit / Sports") }
                )
            }
        }

        // Section 1: Entity & Identity Labels
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "1. IDENTIFIER & PROFILE LABELS",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = personLabel,
                            onValueChange = { personLabel = it },
                            label = { Text("Person Title") },
                            placeholder = { Text("e.g. Cadet, Trainee, Aspirant") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("config_person_label"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = idLabel,
                            onValueChange = { idLabel = it },
                            label = { Text("ID Code Label") },
                            placeholder = { Text("e.g. Roll No, Chest No, Token") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("config_id_label"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = groupLabel,
                            onValueChange = { groupLabel = it },
                            label = { Text("Grouping Label") },
                            placeholder = { Text("e.g. Batch, Squad, Company") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("config_group_label"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = categoryLabel,
                            onValueChange = { categoryLabel = it },
                            label = { Text("Category / Role") },
                            placeholder = { Text("e.g. Target Exam, Trade") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("config_category_label"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = availableBatches,
                        onValueChange = { availableBatches = it },
                        label = { Text("Available ${groupLabel}es (Comma-separated)") },
                        placeholder = { Text("Alpha, Bravo, Charlie, Delta") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_batches_list"),
                        maxLines = 2
                    )
                }
            }
        }

        // Section 2: Physical Drill & Metric Labels
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "2. DRILL & PERFORMANCE METRIC NAMES",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = metricRunLabel,
                        onValueChange = { metricRunLabel = it },
                        label = { Text("Primary Run Drill Label") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("config_run_label"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = metricPullUpsLabel,
                            onValueChange = { metricPullUpsLabel = it },
                            label = { Text("Pull-ups / Beam") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = metricPushUpsLabel,
                            onValueChange = { metricPushUpsLabel = it },
                            label = { Text("Push-ups Drill") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = metricDandBaithakLabel,
                            onValueChange = { metricDandBaithakLabel = it },
                            label = { Text("Squats / Dand-Baithak") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = metricSitUpsLabel,
                            onValueChange = { metricSitUpsLabel = it },
                            label = { Text("Sit-ups / Core") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = qualifier1Label,
                            onValueChange = { qualifier1Label = it },
                            label = { Text("Qualifier 1 Test") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = qualifier2Label,
                            onValueChange = { qualifier2Label = it },
                            label = { Text("Qualifier 2 Test") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }
        }

        // Save & Reset Buttons
        item {
            if (saveFeedback) {
                Surface(
                    color = PetGroup1Green.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PetGroup1Green, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "✓ All custom field labels saved and updated across app!",
                        color = PetGroup1Green,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val updated = FieldConfig(
                        personLabel = personLabel.trim().ifBlank { "Cadet" },
                        idLabel = idLabel.trim().ifBlank { "Roll No" },
                        groupLabel = groupLabel.trim().ifBlank { "Batch" },
                        categoryLabel = categoryLabel.trim().ifBlank { "Target Exam" },
                        metricRunLabel = metricRunLabel.trim().ifBlank { "1600m Run Timing" },
                        metricPullUpsLabel = metricPullUpsLabel.trim().ifBlank { "Pull-ups (Beam)" },
                        metricPushUpsLabel = metricPushUpsLabel.trim().ifBlank { "Push-ups" },
                        metricDandBaithakLabel = metricDandBaithakLabel.trim().ifBlank { "Dand-Baithak (Squats)" },
                        metricSitUpsLabel = metricSitUpsLabel.trim().ifBlank { "Sit-ups" },
                        qualifier1Label = qualifier1Label.trim().ifBlank { "9-ft Ditch Jump" },
                        qualifier2Label = qualifier2Label.trim().ifBlank { "Zig-zag Balance" },
                        availableBatches = availableBatches.trim().ifBlank { "Alpha, Bravo, Charlie" }
                    )
                    onSaveConfig(updated)
                    saveFeedback = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_config_button"),
                colors = ButtonDefaults.buttonColors(containerColor = ArmyGreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE CUSTOMIZED FIELD LABELS",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onResetConfig()
                    saveFeedback = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_config_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reset to Defense Standards")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
