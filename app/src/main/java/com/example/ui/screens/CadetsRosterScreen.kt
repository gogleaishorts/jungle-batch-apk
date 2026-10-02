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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Cadet
import com.example.data.model.FieldConfig
import com.example.ui.components.CadetAvatar
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.viewmodel.UserRole

@Composable
fun CadetsRosterScreen(
    cadets: List<Cadet>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedBatch: String,
    onBatchSelect: (String) -> Unit,
    currentRole: UserRole,
    fieldConfig: FieldConfig,
    onSelectCadet: (Long) -> Unit,
    onEditCadet: (Cadet) -> Unit,
    onAddCadet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val batches = listOf("All") + fieldConfig.batchList

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by ${fieldConfig.personLabel.lowercase()}, ${fieldConfig.idLabel.lowercase()}...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ArmyGreenPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("search_cadets_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Batch Filter Chips/Tabs
            ScrollableTabRow(
                selectedTabIndex = batches.indexOf(selectedBatch).coerceAtLeast(0),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ArmyGreenPrimary,
                edgePadding = 16.dp
            ) {
                batches.forEach { batch ->
                    Tab(
                        selected = selectedBatch == batch,
                        onClick = { onBatchSelect(batch) },
                        text = {
                            Text(
                                text = batch,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedBatch == batch) FontWeight.Black else FontWeight.Normal
                                )
                            )
                        },
                        selectedContentColor = ArmyGreenPrimary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cadet List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ENROLLED ${fieldConfig.personLabel.uppercase()}S (${cadets.size})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (cadets.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No ${fieldConfig.personLabel.lowercase()}s found matching search criteria.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(cadets) { cadet ->
                        CadetRosterCard(
                            cadet = cadet,
                            fieldConfig = fieldConfig,
                            onClick = { onSelectCadet(cadet.id) },
                            onEdit = { onEditCadet(cadet) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Add Cadet Floating Action Button
        FloatingActionButton(
            onClick = onAddCadet,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("enroll_new_cadet_fab"),
            containerColor = ArmyGreenPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = "Add ${fieldConfig.personLabel}")
        }
    }
}

@Composable
fun CadetRosterCard(
    cadet: Cadet,
    fieldConfig: FieldConfig,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CadetAvatar(name = cadet.fullName, batch = cadet.batchName, sizeDp = 48)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = cadet.fullName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                    Text(
                        text = cadet.rollNo,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = ArmyGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${cadet.batchName} • ${cadet.targetExam}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Ht: ${cadet.heightCm.toInt()}cm",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = "•", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "Wt: ${cadet.weightKg.toInt()}kg",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = "•", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "Chest Exp: +${cadet.chestExpansionCm.toInt()}cm",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArmyGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${fieldConfig.personLabel}",
                    tint = ArmyGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View Dossier",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
