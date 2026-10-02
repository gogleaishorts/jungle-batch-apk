package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Cadet
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog
import com.example.ui.components.CadetAvatar
import com.example.ui.components.PetBadge
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.PetFailRed
import com.example.ui.theme.PetGroup1Green
import com.example.ui.theme.TacticalGold
import com.example.ui.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalRegisterScreen(
    cadets: List<Cadet>,
    logs: List<PhysicalLog>,
    currentRole: UserRole,
    activeCadetId: Long?,
    fieldConfig: FieldConfig,
    onAddLog: (PhysicalLog) -> Unit,
    onUpdateLog: (PhysicalLog) -> Unit,
    onDeleteLog: (PhysicalLog) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingLog by remember { mutableStateOf<PhysicalLog?>(null) }
    var showStopwatch by remember { mutableStateOf(false) }
    var measuredRunSeconds by remember { mutableIntStateOf(330) }

    val activeCadet = cadets.firstOrNull { it.id == activeCadetId } ?: cadets.firstOrNull()

    // Filter logs if in Cadet mode
    val displayedLogs = if (currentRole == UserRole.CADET && activeCadet != null) {
        logs.filter { it.cadetId == activeCadet.id }
    } else {
        logs
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Header Banner
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
                                    text = "DIGITAL DRILL REGISTER",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "Manage, edit & create performance logs with auto date-stamps",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TacticalGold
                                )
                            }
                            Button(
                                onClick = { showStopwatch = true },
                                colors = ButtonDefaults.buttonColors(containerColor = TacticalGold),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("launch_stopwatch_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ArmyGreenDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Timer",
                                    color = ArmyGreenDark,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = Color(0xFF131D16),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        fieldConfig.metricRunLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TacticalGold
                                    )
                                    Text(
                                        "≤ 5m 30s (Grp 1)",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        fieldConfig.metricPullUpsLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TacticalGold
                                    )
                                    Text(
                                        "10 Reps = 40 Marks",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        fieldConfig.metricPushUpsLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TacticalGold
                                    )
                                    Text(
                                        "Cadence Count",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentRole == UserRole.CADET) "YOUR DRILL RECORDS" else "BATCH DRILL ENTRIES (${displayedLogs.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Total Entries: ${displayedLogs.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (displayedLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No physical drill entries recorded yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(displayedLogs) { log ->
                    val cadet = cadets.firstOrNull { it.id == log.cadetId }
                    PhysicalLogCard(
                        log = log,
                        fieldConfig = fieldConfig,
                        cadetName = cadet?.fullName ?: "Person #${log.cadetId}",
                        cadetRoll = cadet?.rollNo ?: "ID-000",
                        batchName = cadet?.batchName ?: "General",
                        onEdit = { editingLog = log },
                        onDelete = { onDeleteLog(log) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_physical_log_fab"),
            containerColor = ArmyGreenPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Record Drill Entry")
        }
    }

    if (showStopwatch) {
        StopwatchDrillDialog(
            initialCadetName = activeCadet?.fullName ?: "Current Person",
            onSaveRunTime = { sec ->
                measuredRunSeconds = sec
                showAddDialog = true
            },
            onDismiss = { showStopwatch = false }
        )
    }

    if (showAddDialog) {
        ManagePhysicalLogDialog(
            logToEdit = null,
            cadets = cadets,
            fieldConfig = fieldConfig,
            initialCadetId = activeCadetId ?: cadets.firstOrNull()?.id ?: 1L,
            initialRunSeconds = measuredRunSeconds,
            onSave = { newLog ->
                onAddLog(newLog)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    if (editingLog != null) {
        ManagePhysicalLogDialog(
            logToEdit = editingLog,
            cadets = cadets,
            fieldConfig = fieldConfig,
            initialCadetId = editingLog!!.cadetId,
            initialRunSeconds = editingLog!!.runTimeSeconds,
            onSave = { updatedLog ->
                onUpdateLog(updatedLog)
                editingLog = null
            },
            onDismiss = { editingLog = null }
        )
    }
}

@Composable
fun PhysicalLogCard(
    log: PhysicalLog,
    fieldConfig: FieldConfig,
    cadetName: String,
    cadetRoll: String,
    batchName: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(log.logDate))
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Drill Entry?") },
            text = { Text("Are you sure you want to delete this drill log for $cadetName on $dateStr?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PetFailRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CadetAvatar(name = cadetName, batch = batchName, sizeDp = 36)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = cadetName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$cadetRoll • $batchName",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PetBadge(groupText = log.runPetGroup)
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Log", tint = ArmyGreenPrimary, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Log", tint = PetFailRed, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics Grid
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricMiniItem(
                        label = fieldConfig.metricRunLabel,
                        value = log.formattedRunTime,
                        sub = "${log.runMarks} Mks"
                    )
                    MetricMiniItem(
                        label = fieldConfig.metricPullUpsLabel,
                        value = "${log.pullUpsCount}",
                        sub = "${log.pullUpsMarks} Mks"
                    )
                    MetricMiniItem(
                        label = fieldConfig.metricPushUpsLabel,
                        value = "${log.pushUpsCount}",
                        sub = "Reps"
                    )
                    MetricMiniItem(
                        label = fieldConfig.metricDandBaithakLabel,
                        value = "${log.dandBaithakCount}",
                        sub = "Reps"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score & Qualifiers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = null,
                        tint = TacticalGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Total PET Score: ${log.totalPetMarks}/100",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                        color = ArmyGreenPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${fieldConfig.qualifier1Label}: ${if (log.ditchJumpPass) "PASS" else "FAIL"}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (log.ditchJumpPass) PetGroup1Green else Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${fieldConfig.qualifier2Label}: ${if (log.zigZagPass) "PASS" else "FAIL"}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (log.zigZagPass) PetGroup1Green else Color.Red
                    )
                }
            }

            if (log.coachNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Instructor Note: \"${log.coachNotes}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Recorded: $dateStr",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun MetricMiniItem(label: String, value: String, sub: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = sub,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = ArmyGreenPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePhysicalLogDialog(
    logToEdit: PhysicalLog? = null,
    cadets: List<Cadet>,
    fieldConfig: FieldConfig,
    initialCadetId: Long,
    initialRunSeconds: Int,
    onSave: (PhysicalLog) -> Unit,
    onDismiss: () -> Unit
) {
    val isEdit = logToEdit != null
    var selectedCadetId by remember { mutableStateOf(logToEdit?.cadetId ?: initialCadetId) }
    var cadetExpanded by remember { mutableStateOf(false) }

    val runSeconds = logToEdit?.runTimeSeconds ?: initialRunSeconds
    var runMinutesText by remember { mutableStateOf((runSeconds / 60).toString()) }
    var runSecondsText by remember { mutableStateOf(String.format("%02d", runSeconds % 60)) }
    var pushUpsText by remember { mutableStateOf((logToEdit?.pushUpsCount ?: 40).toString()) }
    var sitUpsText by remember { mutableStateOf((logToEdit?.sitUpsCount ?: 45).toString()) }
    var dandBaithakText by remember { mutableStateOf((logToEdit?.dandBaithakCount ?: 70).toString()) }
    var pullUpsText by remember { mutableStateOf((logToEdit?.pullUpsCount ?: 10).toString()) }
    var ditchJumpPass by remember { mutableStateOf(logToEdit?.ditchJumpPass ?: true) }
    var zigZagPass by remember { mutableStateOf(logToEdit?.zigZagPass ?: true) }
    var coachNotes by remember { mutableStateOf(logToEdit?.coachNotes ?: "") }

    val currentCadet = cadets.firstOrNull { it.id == selectedCadetId } ?: cadets.firstOrNull()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsRun,
                    contentDescription = null,
                    tint = TacticalGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEdit) "EDIT DRILL ENTRY" else "RECORD NEW DRILL ENTRY",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Select Cadet / Person
                ExposedDropdownMenuBox(
                    expanded = cadetExpanded,
                    onExpandedChange = { cadetExpanded = !cadetExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = currentCadet?.let { "${it.fullName} (${it.rollNo})" } ?: "Select ${fieldConfig.personLabel}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(fieldConfig.personLabel) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cadetExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("log_cadet_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = cadetExpanded,
                        onDismissRequest = { cadetExpanded = false }
                    ) {
                        cadets.forEach { cadet ->
                            DropdownMenuItem(
                                text = { Text("${cadet.fullName} (${cadet.rollNo})") },
                                onClick = {
                                    selectedCadetId = cadet.id
                                    cadetExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = fieldConfig.metricRunLabel.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = runMinutesText,
                        onValueChange = { runMinutesText = it },
                        label = { Text("Minutes") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_run_min_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = runSecondsText,
                        onValueChange = { runSecondsText = it },
                        label = { Text("Seconds") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_run_sec_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "REPS & STRENGTH COUNTS",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = pushUpsText,
                        onValueChange = { pushUpsText = it },
                        label = { Text(fieldConfig.metricPushUpsLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_pushups_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = pullUpsText,
                        onValueChange = { pullUpsText = it },
                        label = { Text(fieldConfig.metricPullUpsLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_pullups_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = dandBaithakText,
                        onValueChange = { dandBaithakText = it },
                        label = { Text(fieldConfig.metricDandBaithakLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_dand_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = sitUpsText,
                        onValueChange = { sitUpsText = it },
                        label = { Text(fieldConfig.metricSitUpsLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("log_situps_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Qualifiers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ditchJumpPass,
                        onCheckedChange = { ditchJumpPass = it }
                    )
                    Text("${fieldConfig.qualifier1Label} (Passed)", style = MaterialTheme.typography.bodySmall)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = zigZagPass,
                        onCheckedChange = { zigZagPass = it }
                    )
                    Text("${fieldConfig.qualifier2Label} (Passed)", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = coachNotes,
                    onValueChange = { coachNotes = it },
                    label = { Text("Evaluation Remarks") },
                    placeholder = { Text("e.g. Good pacing, clean form") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val m = runMinutesText.toIntOrNull() ?: 5
                    val s = runSecondsText.toIntOrNull() ?: 30
                    val totalSec = m * 60 + s
                    val push = pushUpsText.toIntOrNull() ?: 35
                    val sit = sitUpsText.toIntOrNull() ?: 40
                    val dand = dandBaithakText.toIntOrNull() ?: 60
                    val pull = pullUpsText.toIntOrNull() ?: 9

                    val log = logToEdit?.copy(
                        cadetId = selectedCadetId,
                        runTimeSeconds = totalSec,
                        pushUpsCount = push,
                        sitUpsCount = sit,
                        dandBaithakCount = dand,
                        pullUpsCount = pull,
                        ditchJumpPass = ditchJumpPass,
                        zigZagPass = zigZagPass,
                        coachNotes = coachNotes.trim()
                    ) ?: PhysicalLog(
                        cadetId = selectedCadetId,
                        runDistanceMeters = 1600,
                        runTimeSeconds = totalSec,
                        pushUpsCount = push,
                        sitUpsCount = sit,
                        dandBaithakCount = dand,
                        pullUpsCount = pull,
                        ditchJumpPass = ditchJumpPass,
                        zigZagPass = zigZagPass,
                        coachNotes = coachNotes.trim()
                    )
                    onSave(log)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ArmyGreenPrimary),
                modifier = Modifier.testTag("submit_physical_log_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isEdit) "Update Entry" else "Save Entry")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
