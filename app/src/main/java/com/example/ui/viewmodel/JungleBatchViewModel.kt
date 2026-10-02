package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.JungleBatchDatabase
import com.example.data.model.Cadet
import com.example.data.model.DailyWorkoutPlan
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog
import com.example.data.repository.JungleBatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    COACH,
    CADET
}

data class LeaderboardEntry(
    val cadet: Cadet,
    val bestRunSeconds: Int?,
    val formattedBestRun: String,
    val maxPushUps: Int,
    val maxDandBaithak: Int,
    val maxPullUps: Int,
    val maxSitUps: Int,
    val maxBeamMarks: Int,
    val compositePetScore: Int,
    val overallScore: Float,
    val rank: Int = 1
)

class JungleBatchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JungleBatchRepository

    init {
        val database = JungleBatchDatabase.getDatabase(application)
        repository = JungleBatchRepository(
            database.cadetDao(),
            database.physicalLogDao(),
            database.dailyWorkoutPlanDao(),
            database.fieldConfigDao()
        )
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Role state
    private val _currentRole = MutableStateFlow(UserRole.COACH)
    val currentRole: StateFlow<UserRole> = _currentRole

    // Active Cadet selection
    private val _selectedCadetId = MutableStateFlow<Long?>(1L)
    val selectedCadetId: StateFlow<Long?> = _selectedCadetId

    // Logged-in Cadet session
    private val _loggedInCadetId = MutableStateFlow<Long?>(1L)
    val loggedInCadetId: StateFlow<Long?> = _loggedInCadetId

    // Search and Batch filter
    val searchQuery = MutableStateFlow("")
    val selectedBatchFilter = MutableStateFlow("All")

    // Database flows
    val allCadets: StateFlow<List<Cadet>> = repository.allCadets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPhysicalLogs: StateFlow<List<PhysicalLog>> = repository.allPhysicalLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestWorkoutPlan: StateFlow<DailyWorkoutPlan?> = repository.latestWorkoutPlan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val fieldConfig: StateFlow<FieldConfig> = repository.fieldConfig
        .combine(MutableStateFlow(FieldConfig())) { dbConfig, defaultVal ->
            dbConfig ?: defaultVal
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FieldConfig())

    // Filtered cadets
    val filteredCadets: StateFlow<List<Cadet>> = combine(
        allCadets,
        searchQuery,
        selectedBatchFilter
    ) { cadets, query, batch ->
        cadets.filter { cadet ->
            val matchesQuery = query.isBlank() ||
                    cadet.fullName.contains(query, ignoreCase = true) ||
                    cadet.rollNo.contains(query, ignoreCase = true) ||
                    cadet.targetExam.contains(query, ignoreCase = true)

            val matchesBatch = batch == "All" || cadet.batchName == batch
            matchesQuery && matchesBatch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Leaderboards computation based entirely on physical drill metrics
    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = combine(
        allCadets,
        allPhysicalLogs
    ) { cadets, logs ->
        val entries = cadets.map { cadet ->
            val cadetLogs = logs.filter { it.cadetId == cadet.id }

            val bestRun = cadetLogs.map { it.runTimeSeconds }.minOrNull()
            val maxPush = cadetLogs.maxOfOrNull { it.pushUpsCount } ?: 0
            val maxDand = cadetLogs.maxOfOrNull { it.dandBaithakCount } ?: 0
            val maxPull = cadetLogs.maxOfOrNull { it.pullUpsCount } ?: 0
            val maxSit = cadetLogs.maxOfOrNull { it.sitUpsCount } ?: 0
            val maxBeam = cadetLogs.maxOfOrNull { it.pullUpsMarks } ?: 0

            // PET run marks: Group 1 (<=330s) = 60, Group 2 (<=345s) = 48, else 0
            val runMarks = when {
                bestRun == null -> 0
                bestRun <= 330 -> 60
                bestRun <= 345 -> 48
                else -> 0
            }
            val compositePet = (runMarks + maxBeam).coerceIn(0, 100)
            val overall = compositePet.toFloat() + (maxPush * 0.1f) + (maxDand * 0.05f)

            val formattedRun = bestRun?.let {
                val m = it / 60
                val s = it % 60
                String.format("%d:%02d", m, s)
            } ?: "--:--"

            LeaderboardEntry(
                cadet = cadet,
                bestRunSeconds = bestRun,
                formattedBestRun = formattedRun,
                maxPushUps = maxPush,
                maxDandBaithak = maxDand,
                maxPullUps = maxPull,
                maxSitUps = maxSit,
                maxBeamMarks = maxBeam,
                compositePetScore = compositePet,
                overallScore = overall
            )
        }

        entries.sortedByDescending { it.overallScore }.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Role switching
    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun selectCadet(id: Long) {
        _selectedCadetId.value = id
    }

    fun selectLoggedInCadet(id: Long) {
        _loggedInCadetId.value = id
        _selectedCadetId.value = id
    }

    fun loginOrRegister(name: String, photoUri: String?, batch: String) {
        viewModelScope.launch {
            val existing = allCadets.value.firstOrNull { it.fullName.equals(name.trim(), ignoreCase = true) }
            if (existing != null) {
                if (photoUri != null && photoUri != existing.photoUri) {
                    val updated = existing.copy(photoUri = photoUri)
                    repository.updateCadet(updated)
                }
                _loggedInCadetId.value = existing.id
                _selectedCadetId.value = existing.id
            } else {
                val nextRoll = generateNextRollNo(allCadets.value)
                val newCadet = Cadet(
                    rollNo = nextRoll,
                    fullName = name.trim(),
                    batchName = batch,
                    photoUri = photoUri
                )
                val newId = repository.insertCadet(newCadet)
                _loggedInCadetId.value = newId
                _selectedCadetId.value = newId
            }
        }
    }

    // Generate unique ID / Roll No based on custom label
    fun generateNextRollNo(cadets: List<Cadet>): String {
        val nextNum = (cadets.size + 1).toString().padStart(3, '0')
        return "JB-2026-$nextNum"
    }

    // Field Customization
    fun updateFieldConfig(config: FieldConfig) {
        viewModelScope.launch {
            repository.saveFieldConfig(config)
        }
    }

    fun resetFieldConfig() {
        viewModelScope.launch {
            repository.saveFieldConfig(FieldConfig())
        }
    }

    // People Record Management CRUD
    fun addCadet(cadet: Cadet) {
        viewModelScope.launch {
            repository.insertCadet(cadet)
        }
    }

    fun updateCadet(cadet: Cadet) {
        viewModelScope.launch {
            repository.updateCadet(cadet)
        }
    }

    fun deleteCadet(cadet: Cadet) {
        viewModelScope.launch {
            repository.deleteCadet(cadet)
        }
    }

    // Physical Log Record Management CRUD
    fun addPhysicalLog(log: PhysicalLog) {
        viewModelScope.launch {
            repository.insertPhysicalLog(log)
        }
    }

    fun updatePhysicalLog(log: PhysicalLog) {
        viewModelScope.launch {
            repository.updatePhysicalLog(log)
        }
    }

    fun deletePhysicalLog(log: PhysicalLog) {
        viewModelScope.launch {
            repository.deletePhysicalLog(log)
        }
    }

    fun updateWorkoutPlan(plan: DailyWorkoutPlan) {
        viewModelScope.launch {
            repository.insertWorkoutPlan(plan)
        }
    }
}
