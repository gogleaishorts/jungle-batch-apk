package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Cadet
import com.example.ui.components.DefenseTopBar
import com.example.ui.screens.AdmissionDialog
import com.example.ui.screens.CadetDetailScreen
import com.example.ui.screens.CadetsRosterScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FieldCustomizerScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginDialog
import com.example.ui.screens.PhysicalRegisterScreen
import com.example.ui.screens.StopwatchDrillDialog
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.JungleBatchTheme
import com.example.ui.theme.TacticalGold
import com.example.ui.viewmodel.JungleBatchViewModel
import com.example.ui.viewmodel.UserRole

enum class AppDestination(val label: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    CADETS("People", Icons.Default.People),
    PHYSICAL("PT Register", Icons.Default.FitnessCenter),
    CUSTOMIZER("Fields Setup", Icons.Default.Tune),
    LEADERBOARD("Leaderboard", Icons.Default.EmojiEvents)
}

class MainActivity : ComponentActivity() {

    private val viewModel: JungleBatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JungleBatchTheme {
                JungleBatchApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun JungleBatchApp(viewModel: JungleBatchViewModel) {
    var currentDestination by remember { mutableStateOf(AppDestination.DASHBOARD) }
    var viewingCadetId by remember { mutableStateOf<Long?>(null) }
    var showAdmissionDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var editingCadet by remember { mutableStateOf<Cadet?>(null) }
    var showStopwatchDialog by remember { mutableStateOf(false) }

    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val allCadets by viewModel.allCadets.collectAsStateWithLifecycle()
    val filteredCadets by viewModel.filteredCadets.collectAsStateWithLifecycle()
    val physicalLogs by viewModel.allPhysicalLogs.collectAsStateWithLifecycle()
    val latestWorkoutPlan by viewModel.latestWorkoutPlan.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboardEntries.collectAsStateWithLifecycle()
    val selectedCadetId by viewModel.selectedCadetId.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val batchFilter by viewModel.selectedBatchFilter.collectAsStateWithLifecycle()
    val fieldConfig by viewModel.fieldConfig.collectAsStateWithLifecycle()

    val activeCadet = allCadets.firstOrNull { it.id == selectedCadetId } ?: allCadets.firstOrNull()

    // Handle back button when viewing a specific cadet's detail
    if (viewingCadetId != null) {
        val cadet = allCadets.firstOrNull { it.id == viewingCadetId }
        if (cadet != null) {
            CadetDetailScreen(
                cadet = cadet,
                logs = physicalLogs,
                fieldConfig = fieldConfig,
                onBack = { viewingCadetId = null },
                onEditCadet = {
                    editingCadet = cadet
                    showAdmissionDialog = true
                },
                onDeleteLog = { log -> viewModel.deletePhysicalLog(log) }
            )
            return
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefenseTopBar(
                currentRole = currentRole,
                onToggleRole = {
                    val nextRole = if (currentRole == UserRole.COACH) UserRole.CADET else UserRole.COACH
                    viewModel.setRole(nextRole)
                },
                activeCadetName = activeCadet?.fullName,
                activeCadetPhotoUri = activeCadet?.photoUri,
                onSwitchUser = { showLoginDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = ArmyGreenDark,
                contentColor = Color.White
            ) {
                AppDestination.entries.forEach { destination ->
                    val isSelected = currentDestination == destination
                    val navLabel = if (destination == AppDestination.CADETS) {
                        "${fieldConfig.personLabel}s"
                    } else {
                        destination.label
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            currentDestination = destination
                            viewingCadetId = null
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = navLabel,
                                tint = if (isSelected) TacticalGold else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        label = {
                            Text(
                                text = navLabel,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) TacticalGold else Color.White.copy(alpha = 0.6f)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = ArmyGreenPrimary.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_${destination.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                AppDestination.DASHBOARD -> {
                    DashboardScreen(
                        currentRole = currentRole,
                        cadets = allCadets,
                        logs = physicalLogs,
                        workoutPlan = latestWorkoutPlan,
                        leaderboard = leaderboard,
                        fieldConfig = fieldConfig,
                        selectedCadetId = selectedCadetId,
                        onSelectCadet = { id -> viewModel.selectCadet(id) },
                        onViewDossier = { id -> viewingCadetId = id },
                        onOpenLogin = { showLoginDialog = true },
                        onNavigateToCadets = { currentDestination = AppDestination.CADETS },
                        onNavigateToPhysical = { currentDestination = AppDestination.PHYSICAL },
                        onNavigateToCustomizer = { currentDestination = AppDestination.CUSTOMIZER },
                        onLaunchStopwatch = { showStopwatchDialog = true },
                        onOpenAdmission = {
                            editingCadet = null
                            showAdmissionDialog = true
                        },
                        onUpdateWod = { plan -> viewModel.updateWorkoutPlan(plan) }
                    )
                }

                AppDestination.CADETS -> {
                    CadetsRosterScreen(
                        cadets = filteredCadets,
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.searchQuery.value = it },
                        selectedBatch = batchFilter,
                        onBatchSelect = { viewModel.selectedBatchFilter.value = it },
                        currentRole = currentRole,
                        fieldConfig = fieldConfig,
                        onSelectCadet = { id -> viewingCadetId = id },
                        onEditCadet = { cadet ->
                            editingCadet = cadet
                            showAdmissionDialog = true
                        },
                        onAddCadet = {
                            editingCadet = null
                            showAdmissionDialog = true
                        }
                    )
                }

                AppDestination.PHYSICAL -> {
                    PhysicalRegisterScreen(
                        cadets = allCadets,
                        logs = physicalLogs,
                        currentRole = currentRole,
                        activeCadetId = selectedCadetId,
                        fieldConfig = fieldConfig,
                        onAddLog = { log -> viewModel.addPhysicalLog(log) },
                        onUpdateLog = { log -> viewModel.updatePhysicalLog(log) },
                        onDeleteLog = { log -> viewModel.deletePhysicalLog(log) }
                    )
                }

                AppDestination.CUSTOMIZER -> {
                    FieldCustomizerScreen(
                        currentConfig = fieldConfig,
                        onSaveConfig = { updated -> viewModel.updateFieldConfig(updated) },
                        onResetConfig = { viewModel.resetFieldConfig() }
                    )
                }

                AppDestination.LEADERBOARD -> {
                    LeaderboardScreen(
                        entries = leaderboard,
                        fieldConfig = fieldConfig,
                        onSelectCadet = { id -> viewingCadetId = id }
                    )
                }
            }
        }
    }

    // Login & Profile Setup Dialog
    if (showLoginDialog) {
        LoginDialog(
            allCadets = allCadets,
            fieldConfig = fieldConfig,
            onLoginSuccess = { name, photoUri, batch ->
                viewModel.loginOrRegister(name, photoUri, batch)
                showLoginDialog = false
            },
            onSelectExisting = { cadet ->
                viewModel.selectLoggedInCadet(cadet.id)
                showLoginDialog = false
            },
            onDismiss = { showLoginDialog = false }
        )
    }

    // Person Management Dialog (Add / Edit / Delete)
    if (showAdmissionDialog) {
        val nextRoll = viewModel.generateNextRollNo(allCadets)
        AdmissionDialog(
            cadetToEdit = editingCadet,
            generatedRollNo = nextRoll,
            fieldConfig = fieldConfig,
            onSaveCadet = { savedCadet ->
                if (editingCadet != null) {
                    viewModel.updateCadet(savedCadet)
                } else {
                    viewModel.addCadet(savedCadet)
                }
                editingCadet = null
                showAdmissionDialog = false
            },
            onDeleteCadet = { toDelete ->
                viewModel.deleteCadet(toDelete)
                if (viewingCadetId == toDelete.id) {
                    viewingCadetId = null
                }
                editingCadet = null
                showAdmissionDialog = false
            },
            onDismiss = {
                editingCadet = null
                showAdmissionDialog = false
            }
        )
    }

    // Drill Stopwatch Dialog
    if (showStopwatchDialog) {
        StopwatchDrillDialog(
            initialCadetName = activeCadet?.fullName ?: "Person",
            onSaveRunTime = { sec ->
                currentDestination = AppDestination.PHYSICAL
            },
            onDismiss = { showStopwatchDialog = false }
        )
    }
}
