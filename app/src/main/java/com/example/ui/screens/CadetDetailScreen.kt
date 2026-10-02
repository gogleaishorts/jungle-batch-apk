package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Cadet
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog
import com.example.ui.components.CadetAvatar
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.PetGroup1Green
import com.example.ui.theme.TacticalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadetDetailScreen(
    cadet: Cadet,
    logs: List<PhysicalLog>,
    fieldConfig: FieldConfig,
    onBack: () -> Unit,
    onEditCadet: () -> Unit,
    onDeleteLog: (PhysicalLog) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    val cadetLogs = logs.filter { it.cadetId == cadet.id }

    val bestRunSec = cadetLogs.map { it.runTimeSeconds }.minOrNull()
    val maxPush = cadetLogs.maxOfOrNull { it.pushUpsCount } ?: 0
    val maxDand = cadetLogs.maxOfOrNull { it.dandBaithakCount } ?: 0
    val maxPull = cadetLogs.maxOfOrNull { it.pullUpsCount } ?: 0
    val maxBeamMarks = cadetLogs.maxOfOrNull { it.pullUpsMarks } ?: 0
    val maxSit = cadetLogs.maxOfOrNull { it.sitUpsCount } ?: 0

    val formattedBestRun = bestRunSec?.let {
        val m = it / 60
        val s = it % 60
        String.format("%d:%02d", m, s)
    } ?: "--:--"

    fun shareReport() {
        val report = buildString {
            appendLine("🎖 JUNGLE BATCH ${fieldConfig.personLabel.uppercase()} DOSSIER 🎖")
            appendLine("Name: ${cadet.fullName} (${cadet.rollNo})")
            appendLine("${fieldConfig.groupLabel}: ${cadet.batchName} | ${fieldConfig.categoryLabel}: ${cadet.targetExam}")
            appendLine("Height: ${cadet.heightCm} cm | Weight: ${cadet.weightKg} kg")
            appendLine("Chest: ${cadet.chestNormalCm} to ${cadet.chestExpandedCm} cm (Expansion: +${cadet.chestExpansionCm} cm)")
            appendLine("-----------------------------")
            appendLine("PHYSICAL EFFICIENCY (PET) RECORD:")
            appendLine("• Best ${fieldConfig.metricRunLabel}: $formattedBestRun")
            appendLine("• ${fieldConfig.metricPullUpsLabel}: $maxPull reps ($maxBeamMarks/40 marks)")
            appendLine("• Max ${fieldConfig.metricPushUpsLabel}: $maxPush reps")
            appendLine("• Max ${fieldConfig.metricDandBaithakLabel}: $maxDand reps")
            appendLine("• Max ${fieldConfig.metricSitUpsLabel}: $maxSit reps")
            appendLine("-----------------------------")
            appendLine("Motto: Train Hard, Win Easy!")
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, report)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share ${fieldConfig.personLabel} Dossier"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${fieldConfig.personLabel.uppercase()} DOSSIER: ${cadet.rollNo}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEditCadet) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Person Record",
                            tint = TacticalGold
                        )
                    }
                    IconButton(onClick = { shareReport() }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Dossier",
                            tint = TacticalGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ArmyGreenDark,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Profile Hero Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TacticalGold, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = ArmyGreenDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CadetAvatar(name = cadet.fullName, batch = cadet.batchName, sizeDp = 56)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = cadet.fullName,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                    color = Color.White
                                )
                                Text(
                                    text = "${cadet.batchName} • Age ${cadet.age} yrs",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TacticalGold
                                )
                                Text(
                                    text = "${fieldConfig.categoryLabel}: ${cadet.targetExam}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Physical Standard Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DossierTag(label = "Height", value = "${cadet.heightCm.toInt()} cm")
                            DossierTag(label = "Weight", value = "${cadet.weightKg.toInt()} kg")
                            DossierTag(label = "Chest", value = "${cadet.chestNormalCm.toInt()}-${cadet.chestExpandedCm.toInt()} cm")
                            DossierTag(label = "Expansion", value = "+${cadet.chestExpansionCm.toInt()} cm")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DossierTag(label = "Blood Group", value = cadet.bloodGroup)
                            DossierTag(label = "Medical Cat", value = cadet.medicalStatus)
                            DossierTag(
                                label = "PST Fit",
                                value = if (cadet.isPhysicalStandardFit) "QUALIFIED" else "FIT",
                                color = PetGroup1Green
                            )
                        }
                    }
                }
            }

            // Key Metrics Summary
            item {
                Text(
                    text = "PERSONAL BEST PHYSICAL RECORDS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MetricHighlightCard(
                        title = fieldConfig.metricRunLabel,
                        value = formattedBestRun,
                        sub = if (bestRunSec != null && bestRunSec <= 330) "Group 1 (60 Mks)" else "Group 2",
                        icon = Icons.Default.DirectionsRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MetricHighlightCard(
                        title = fieldConfig.metricPullUpsLabel,
                        value = "$maxPull Reps",
                        sub = "$maxBeamMarks / 40 Marks",
                        icon = Icons.Default.MilitaryTech,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MetricHighlightCard(
                        title = fieldConfig.metricPushUpsLabel,
                        value = "$maxPush Reps",
                        sub = "Strict Cadence",
                        icon = Icons.Default.FitnessCenter,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MetricHighlightCard(
                        title = fieldConfig.metricDandBaithakLabel,
                        value = "$maxDand Reps",
                        sub = "Squat Endurance",
                        icon = Icons.Default.AssignmentInd,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Logs Timeline
            item {
                Text(
                    text = "DRILL HISTORY TIMELINE (${cadetLogs.size} ENTRIES)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            if (cadetLogs.isEmpty()) {
                item {
                    Text(
                        text = "No drill sessions logged yet for this ${fieldConfig.personLabel.lowercase()}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(cadetLogs) { log ->
                    PhysicalLogCard(
                        log = log,
                        fieldConfig = fieldConfig,
                        cadetName = cadet.fullName,
                        cadetRoll = cadet.rollNo,
                        batchName = cadet.batchName,
                        onEdit = { onEditCadet() },
                        onDelete = { onDeleteLog(log) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DossierTag(label: String, value: String, color: Color = TacticalGold) {
    Surface(
        color = Color(0xFF131D16),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
            Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = color)
        }
    }
}

@Composable
fun MetricHighlightCard(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(imageVector = icon, contentDescription = null, tint = ArmyGreenPrimary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            )
            Text(text = sub, style = MaterialTheme.typography.labelSmall, color = ArmyGreenPrimary)
        }
    }
}
