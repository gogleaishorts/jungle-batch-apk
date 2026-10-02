package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Cadet
import com.example.data.model.DailyWorkoutPlan
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog
import com.example.ui.components.CadetAvatar
import com.example.ui.components.TacticalStatCard
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.PetGroup1Green
import com.example.ui.theme.TacticalGold
import com.example.ui.viewmodel.LeaderboardEntry
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentRole: UserRole,
    cadets: List<Cadet>,
    logs: List<PhysicalLog>,
    workoutPlan: DailyWorkoutPlan?,
    leaderboard: List<LeaderboardEntry>,
    fieldConfig: FieldConfig,
    selectedCadetId: Long?,
    onSelectCadet: (Long) -> Unit,
    onViewDossier: (Long) -> Unit,
    onOpenLogin: () -> Unit,
    onNavigateToCadets: () -> Unit,
    onNavigateToPhysical: () -> Unit,
    onNavigateToCustomizer: () -> Unit,
    onLaunchStopwatch: () -> Unit,
    onOpenAdmission: () -> Unit,
    onUpdateWod: (DailyWorkoutPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditWodDialog by remember { mutableStateOf(false) }

    val activeCadet = cadets.firstOrNull { it.id == selectedCadetId } ?: cadets.firstOrNull()
    val activeCadetLogs = logs.filter { it.cadetId == activeCadet?.id }
    val bestRunSec = activeCadetLogs.map { it.runTimeSeconds }.minOrNull()
    val maxPull = activeCadetLogs.maxOfOrNull { it.pullUpsCount } ?: 0
    val maxPush = activeCadetLogs.maxOfOrNull { it.pushUpsCount } ?: 0
    val maxDand = activeCadetLogs.maxOfOrNull { it.dandBaithakCount } ?: 0

    val formattedBestRun = bestRunSec?.let {
        val m = it / 60
        val s = it % 60
        String.format("%d:%02d", m, s)
    } ?: "--:--"

    // Analytics
    val totalPeople = cadets.size
    val group1RunCount = logs.count { it.runTimeSeconds <= 330 }
    val group1Rate = if (logs.isNotEmpty()) (group1RunCount * 100) / logs.size else 0
    val avgBeamReps = if (logs.isNotEmpty()) logs.map { it.pullUpsCount }.average().toInt() else 0
    val totalPushUpsLogged = logs.sumOf { it.pushUpsCount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // MY LOGGED-IN RECORD CARD
            if (activeCadet != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TacticalGold, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = ArmyGreenDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = TacticalGold,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "MY RECORD (${fieldConfig.personLabel.uppercase()})",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = ArmyGreenDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            OutlinedButton(
                                onClick = onOpenLogin,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "Switch / Sign In",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TacticalGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CadetAvatar(
                                name = activeCadet.fullName,
                                batch = activeCadet.batchName,
                                photoUri = activeCadet.photoUri,
                                sizeDp = 56
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeCadet.fullName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = Color.White
                                )
                                Text(
                                    text = "${activeCadet.rollNo} • ${activeCadet.batchName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TacticalGold
                                )
                                Text(
                                    text = "${fieldConfig.categoryLabel}: ${activeCadet.targetExam}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            Button(
                                onClick = { onViewDossier(activeCadet.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = TacticalGold),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Full Dossier",
                                    color = ArmyGreenDark,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Stats of logged in user
                        Surface(
                            color = Color(0xFF131D16),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(fieldConfig.metricRunLabel, style = MaterialTheme.typography.labelSmall, color = TacticalGold, fontSize = 10.sp)
                                    Text(formattedBestRun, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(fieldConfig.metricPullUpsLabel, style = MaterialTheme.typography.labelSmall, color = TacticalGold, fontSize = 10.sp)
                                    Text("$maxPull reps", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(fieldConfig.metricPushUpsLabel, style = MaterialTheme.typography.labelSmall, color = TacticalGold, fontSize = 10.sp)
                                    Text("$maxPush reps", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Drill Logs", style = MaterialTheme.typography.labelSmall, color = TacticalGold, fontSize = 10.sp)
                                    Text("${activeCadetLogs.size}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // ALL REGISTERED INDIVIDUALS & PHOTOS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ALL LOGGED-IN MEMBERS & PHOTOS (${cadets.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tap to view record",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArmyGreenPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cadets) { cadet ->
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .clickable { onViewDossier(cadet.id) }
                            .border(
                                1.dp,
                                if (cadet.id == activeCadet?.id) TacticalGold else MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(10.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (cadet.id == activeCadet?.id) ArmyGreenDark.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CadetAvatar(
                                name = cadet.fullName,
                                batch = cadet.batchName,
                                photoUri = cadet.photoUri,
                                sizeDp = 48
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = cadet.fullName.split(" ").firstOrNull() ?: cadet.fullName,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1
                            )
                            Text(
                                text = cadet.rollNo,
                                style = MaterialTheme.typography.labelSmall,
                                color = ArmyGreenPrimary
                            )
                        }
                    }
                }
            }
        }

        // Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.jungle_camp_hero),
                        contentDescription = "Defense Academy Obstacle Course",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        ArmyGreenDark.copy(alpha = 0.75f),
                                        ArmyGreenDark.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = TacticalGold,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PHYSICAL DRILL & DATA MANAGEMENT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = ArmyGreenDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "JUNGLE BATCH REGIMENT",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "\"Train Hard in Peace, Win Easy in Battle!\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = TacticalGold
                        )
                    }
                }
            }
        }

        // Quick Operations Grid
        item {
            Text(
                text = "QUICK OPERATIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                QuickActionButton(
                    title = "Stopwatch",
                    icon = Icons.Default.Timer,
                    onClick = onLaunchStopwatch,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(
                    title = "Log Drill",
                    icon = Icons.Default.FitnessCenter,
                    onClick = onNavigateToPhysical,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(
                    title = "Add ${fieldConfig.personLabel}",
                    icon = Icons.Default.PersonAdd,
                    onClick = onOpenAdmission,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickActionButton(
                    title = "Fields",
                    icon = Icons.Default.Tune,
                    onClick = onNavigateToCustomizer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Daily Routine & WOD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TacticalGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = ArmyGreenDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DAILY TRAINING SCHEDULE & WOD",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.8.sp
                                ),
                                color = Color.White
                            )
                        }

                        if (currentRole == UserRole.COACH) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { showEditWodDialog = true },
                                color = Color(0xFF131D16)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit WOD", tint = TacticalGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Edit", color = TacticalGold, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Focus: ${workoutPlan?.focusArea ?: "1600m Speed Simulation & Beam Conditioning"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TacticalGold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WodDrillItem(
                        tag = "MORNING DRILL",
                        drill = workoutPlan?.morningDrill ?: "15m warm-up + 1600m Run + 60 Dand-Baithak"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    WodDrillItem(
                        tag = "EVENING DRILL",
                        drill = workoutPlan?.eveningDrill ?: "5km endurance jog + Pull-up sets + Core plank"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = Color(0xFF131D16),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Commander Quote: \"${workoutPlan?.coachQuote ?: "Drill hard in peace so you bleed less in combat!"}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // Batch Analytics Stat Cards
        item {
            Text(
                text = "${fieldConfig.groupLabel.uppercase()} ANALYTICS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TacticalStatCard(
                    title = "Enrolled ${fieldConfig.personLabel}s",
                    value = "$totalPeople",
                    subtitle = "Across ${fieldConfig.groupLabel}s",
                    icon = Icons.Default.Group,
                    accentColor = ArmyGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TacticalStatCard(
                    title = "${fieldConfig.metricRunLabel} Grp 1",
                    value = "$group1Rate%",
                    subtitle = "Sprint Timing ≤ 5:30",
                    icon = Icons.Default.DirectionsRun,
                    accentColor = PetGroup1Green,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TacticalStatCard(
                    title = "Avg ${fieldConfig.metricPullUpsLabel}",
                    value = "$avgBeamReps Reps",
                    subtitle = "Target: 10 Pull-ups",
                    icon = Icons.Default.FitnessCenter,
                    accentColor = TacticalGold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TacticalStatCard(
                    title = "Total Reps Logged",
                    value = "$totalPushUpsLogged",
                    subtitle = fieldConfig.metricPushUpsLabel,
                    icon = Icons.Default.MilitaryTech,
                    accentColor = Color(0xFF2980B9),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Hall of Fame
        item {
            Text(
                text = "HALL OF FAME (TOP PERFORMER)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            val topPerformer = leaderboard.firstOrNull()
            if (topPerformer != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CadetAvatar(
                            name = topPerformer.cadet.fullName,
                            batch = topPerformer.cadet.batchName,
                            sizeDp = 48
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = TacticalGold,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "#1 OVERALL CHAMPION",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topPerformer.cadet.fullName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                            )
                            Text(
                                text = "${topPerformer.cadet.rollNo} • ${fieldConfig.metricRunLabel}: ${topPerformer.formattedBestRun} • ${fieldConfig.metricPullUpsLabel}: ${topPerformer.maxPullUps} reps",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showEditWodDialog && workoutPlan != null) {
        EditWodDialog(
            currentPlan = workoutPlan,
            onSave = { updated ->
                onUpdateWod(updated)
                showEditWodDialog = false
            },
            onDismiss = { showEditWodDialog = false }
        )
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ArmyGreenPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ArmyGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun WodDrillItem(tag: String, drill: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = Color(0xFF131D16),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                color = TacticalGold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = drill,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EditWodDialog(
    currentPlan: DailyWorkoutPlan,
    onSave: (DailyWorkoutPlan) -> Unit,
    onDismiss: () -> Unit
) {
    var focusArea by remember { mutableStateOf(currentPlan.focusArea) }
    var morningDrill by remember { mutableStateOf(currentPlan.morningDrill) }
    var eveningDrill by remember { mutableStateOf(currentPlan.eveningDrill) }
    var academicTarget by remember { mutableStateOf(currentPlan.academicTarget) }
    var coachQuote by remember { mutableStateOf(currentPlan.coachQuote) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "UPDATE DAILY DRILL WOD",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = focusArea,
                    onValueChange = { focusArea = it },
                    label = { Text("Daily Focus Area") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = morningDrill,
                    onValueChange = { morningDrill = it },
                    label = { Text("Morning Drill") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = eveningDrill,
                    onValueChange = { eveningDrill = it },
                    label = { Text("Evening Drill") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = academicTarget,
                    onValueChange = { academicTarget = it },
                    label = { Text("Tactical Target") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = coachQuote,
                    onValueChange = { coachQuote = it },
                    label = { Text("Commander's Quote") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = currentPlan.copy(
                        focusArea = focusArea.trim(),
                        morningDrill = morningDrill.trim(),
                        eveningDrill = eveningDrill.trim(),
                        academicTarget = academicTarget.trim(),
                        coachQuote = coachQuote.trim()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ArmyGreenPrimary)
            ) {
                Text("Save WOD")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
