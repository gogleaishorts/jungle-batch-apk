package com.example.ui.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FieldConfig
import com.example.ui.components.CadetAvatar
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.TacticalGold
import com.example.ui.viewmodel.LeaderboardEntry

data class LeaderboardCategoryItem(
    val title: String,
    val icon: ImageVector,
    val sortKey: String
)

@Composable
fun LeaderboardScreen(
    entries: List<LeaderboardEntry>,
    fieldConfig: FieldConfig,
    onSelectCadet: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    val categories = listOf(
        LeaderboardCategoryItem("Overall Merit", Icons.Default.EmojiEvents, "overall"),
        LeaderboardCategoryItem(fieldConfig.metricRunLabel, Icons.Default.DirectionsRun, "run"),
        LeaderboardCategoryItem(fieldConfig.metricPullUpsLabel, Icons.Default.MilitaryTech, "pull"),
        LeaderboardCategoryItem("${fieldConfig.metricPushUpsLabel} & Squats", Icons.Default.FitnessCenter, "push"),
        LeaderboardCategoryItem(fieldConfig.metricSitUpsLabel, Icons.Default.FitnessCenter, "sit")
    )

    val currentCat = categories.getOrElse(selectedCategoryIndex) { categories.first() }

    val sortedEntries = when (currentCat.sortKey) {
        "overall" -> entries.sortedByDescending { it.overallScore }
        "run" -> entries.sortedBy { it.bestRunSeconds ?: 9999 }
        "pull" -> entries.sortedByDescending { it.maxPullUps }
        "push" -> entries.sortedByDescending { it.maxPushUps + it.maxDandBaithak }
        "sit" -> entries.sortedByDescending { it.maxSitUps }
        else -> entries.sortedByDescending { it.overallScore }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = ArmyGreenDark,
            contentColor = TacticalGold,
            edgePadding = 12.dp
        ) {
            categories.forEachIndexed { index, cat ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.title,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    },
                    selectedContentColor = TacticalGold,
                    unselectedContentColor = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = TacticalGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${fieldConfig.personLabel} Merit & Physical Performance Standings",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Top 3 Podium
            if (sortedEntries.size >= 3) {
                item {
                    PodiumSection(
                        topThree = sortedEntries.take(3),
                        sortKey = currentCat.sortKey,
                        fieldConfig = fieldConfig,
                        onSelectCadet = onSelectCadet
                    )
                }
            }

            item {
                Text(
                    text = "${fieldConfig.personLabel.uppercase()} RANKINGS (${sortedEntries.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            itemsIndexed(sortedEntries) { index, item ->
                LeaderboardRowItem(
                    rank = index + 1,
                    entry = item,
                    sortKey = currentCat.sortKey,
                    fieldConfig = fieldConfig,
                    onClick = { onSelectCadet(item.cadet.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun PodiumSection(
    topThree: List<LeaderboardEntry>,
    sortKey: String,
    fieldConfig: FieldConfig,
    onSelectCadet: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TacticalGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ArmyGreenDark)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TOP PERFORMERS PODIUM",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                ),
                color = TacticalGold
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (topThree.size > 1) {
                    PodiumPillar(
                        rank = 2,
                        entry = topThree[1],
                        sortKey = sortKey,
                        fieldConfig = fieldConfig,
                        medalColor = Color(0xFFC0C0C0),
                        heightDp = 100,
                        onClick = { onSelectCadet(topThree[1].cadet.id) }
                    )
                }

                PodiumPillar(
                    rank = 1,
                    entry = topThree[0],
                    sortKey = sortKey,
                    fieldConfig = fieldConfig,
                    medalColor = TacticalGold,
                    heightDp = 125,
                    onClick = { onSelectCadet(topThree[0].cadet.id) }
                )

                if (topThree.size > 2) {
                    PodiumPillar(
                        rank = 3,
                        entry = topThree[2],
                        sortKey = sortKey,
                        fieldConfig = fieldConfig,
                        medalColor = Color(0xFFCD7F32),
                        heightDp = 85,
                        onClick = { onSelectCadet(topThree[2].cadet.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PodiumPillar(
    rank: Int,
    entry: LeaderboardEntry,
    sortKey: String,
    fieldConfig: FieldConfig,
    medalColor: Color,
    heightDp: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .width(90.dp)
    ) {
        CadetAvatar(name = entry.cadet.fullName, batch = entry.cadet.batchName, sizeDp = if (rank == 1) 48 else 40)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = entry.cadet.fullName.split(" ").firstOrNull() ?: entry.cadet.fullName,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = formatMetricValue(entry, sortKey),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
            color = medalColor
        )
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier
                .width(76.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            color = Color(0xFF131D16)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(medalColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$rank",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardRowItem(
    rank: Int,
    entry: LeaderboardEntry,
    sortKey: String,
    fieldConfig: FieldConfig,
    onClick: () -> Unit
) {
    val rankBadgeBg = when (rank) {
        1 -> TacticalGold
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val rankBadgeFg = if (rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(rankBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                    color = rankBadgeFg
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            CadetAvatar(name = entry.cadet.fullName, batch = entry.cadet.batchName, sizeDp = 40)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.cadet.fullName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Text(
                    text = "${entry.cadet.rollNo} • ${entry.cadet.batchName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatMetricValue(entry, sortKey),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = ArmyGreenPrimary
                )
                Text(
                    text = formatMetricSub(entry, sortKey),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun formatMetricValue(entry: LeaderboardEntry, sortKey: String): String {
    return when (sortKey) {
        "overall" -> "${entry.compositePetScore} PET pts"
        "run" -> entry.formattedBestRun
        "pull" -> "${entry.maxPullUps} beam"
        "push" -> "${entry.maxPushUps + entry.maxDandBaithak} reps"
        "sit" -> "${entry.maxSitUps} reps"
        else -> "${entry.compositePetScore} pts"
    }
}

fun formatMetricSub(entry: LeaderboardEntry, sortKey: String): String {
    return when (sortKey) {
        "overall" -> "Score: ${entry.compositePetScore}/100"
        "run" -> if (entry.bestRunSeconds != null && entry.bestRunSeconds <= 330) "Group 1 (60M)" else "Group 2"
        "pull" -> "${entry.maxBeamMarks}/40 marks"
        "push" -> "Push: ${entry.maxPushUps} | Dand: ${entry.maxDandBaithak}"
        "sit" -> "Sit-ups Reps"
        else -> "Physical Drill"
    }
}
