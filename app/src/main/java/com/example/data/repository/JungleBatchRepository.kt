package com.example.data.repository

import com.example.data.local.CadetDao
import com.example.data.local.DailyWorkoutPlanDao
import com.example.data.local.FieldConfigDao
import com.example.data.local.PhysicalLogDao
import com.example.data.model.Cadet
import com.example.data.model.DailyWorkoutPlan
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog
import kotlinx.coroutines.flow.Flow

class JungleBatchRepository(
    private val cadetDao: CadetDao,
    private val physicalLogDao: PhysicalLogDao,
    private val dailyWorkoutPlanDao: DailyWorkoutPlanDao,
    private val fieldConfigDao: FieldConfigDao
) {
    val allCadets: Flow<List<Cadet>> = cadetDao.getAllCadets()
    val allPhysicalLogs: Flow<List<PhysicalLog>> = physicalLogDao.getAllLogs()
    val latestWorkoutPlan: Flow<DailyWorkoutPlan?> = dailyWorkoutPlanDao.getLatestPlan()
    val allWorkoutPlans: Flow<List<DailyWorkoutPlan>> = dailyWorkoutPlanDao.getAllPlans()
    val fieldConfig: Flow<FieldConfig?> = fieldConfigDao.getFieldConfig()

    fun getCadetById(id: Long): Flow<Cadet?> = cadetDao.getCadetById(id)

    fun getLogsForCadet(cadetId: Long): Flow<List<PhysicalLog>> =
        physicalLogDao.getLogsForCadet(cadetId)

    suspend fun insertCadet(cadet: Cadet): Long = cadetDao.insertCadet(cadet)

    suspend fun updateCadet(cadet: Cadet) = cadetDao.updateCadet(cadet)

    suspend fun deleteCadet(cadet: Cadet) = cadetDao.deleteCadet(cadet)

    suspend fun insertPhysicalLog(log: PhysicalLog): Long =
        physicalLogDao.insertLog(log)

    suspend fun updatePhysicalLog(log: PhysicalLog) =
        physicalLogDao.insertLog(log) // OnConflictStrategy.REPLACE

    suspend fun deletePhysicalLog(log: PhysicalLog) =
        physicalLogDao.deleteLog(log)

    suspend fun insertWorkoutPlan(plan: DailyWorkoutPlan): Long =
        dailyWorkoutPlanDao.insertPlan(plan)

    suspend fun saveFieldConfig(config: FieldConfig) =
        fieldConfigDao.saveFieldConfig(config)

    suspend fun seedInitialDataIfEmpty() {
        val count = cadetDao.getCadetCount()
        if (count == 0) {
            val defaultConfig = FieldConfig()
            fieldConfigDao.saveFieldConfig(defaultConfig)

            val initialCadets = listOf(
                Cadet(
                    rollNo = "JB-2026-001",
                    fullName = "Vikramaditya Rathore",
                    age = 20,
                    phone = "+91 98765 43210",
                    batchName = "Alpha Commando",
                    targetExam = "Indian Army Soldier GD",
                    heightCm = 176.0f,
                    weightKg = 69.5f,
                    chestNormalCm = 82.0f,
                    chestExpandedCm = 88.0f,
                    bloodGroup = "O+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_1",
                    notes = "Exceptional 1.6km timing. Squad Captain potential."
                ),
                Cadet(
                    rollNo = "JB-2026-002",
                    fullName = "Suraj Pratap Gurjar",
                    age = 19,
                    phone = "+91 97654 32109",
                    batchName = "Alpha Commando",
                    targetExam = "Indian Army GD",
                    heightCm = 173.5f,
                    weightKg = 67.0f,
                    chestNormalCm = 80.5f,
                    chestExpandedCm = 86.5f,
                    bloodGroup = "B+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_2",
                    notes = "Strong Dand-Baithak endurance. Consistent Group 1 runner."
                ),
                Cadet(
                    rollNo = "JB-2026-003",
                    fullName = "Aman Kumar Yadav",
                    age = 21,
                    phone = "+91 96543 21098",
                    batchName = "Bravo Agnipath",
                    targetExam = "Indian Army Technical",
                    heightCm = 175.0f,
                    weightKg = 71.0f,
                    chestNormalCm = 81.0f,
                    chestExpandedCm = 87.0f,
                    bloodGroup = "AB+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_3",
                    notes = "Excellent stamina and discipline."
                ),
                Cadet(
                    rollNo = "JB-2026-004",
                    fullName = "Deepak Choudhary",
                    age = 20,
                    phone = "+91 95432 10987",
                    batchName = "Bravo Agnipath",
                    targetExam = "SSC GD Constable",
                    heightCm = 171.0f,
                    weightKg = 65.0f,
                    chestNormalCm = 79.5f,
                    chestExpandedCm = 85.0f,
                    bloodGroup = "A+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_4",
                    notes = "Great pull-up endurance (10+ reps beam)."
                ),
                Cadet(
                    rollNo = "JB-2026-005",
                    fullName = "Kavita Shekhawat",
                    age = 20,
                    phone = "+91 94321 09876",
                    batchName = "Charlie Women Corps",
                    targetExam = "Army Military Police (CMP)",
                    heightCm = 166.0f,
                    weightKg = 56.5f,
                    chestNormalCm = 77.0f,
                    chestExpandedCm = 82.5f,
                    bloodGroup = "O+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_5",
                    notes = "Fast sprinter with great obstacle balance."
                ),
                Cadet(
                    rollNo = "JB-2026-006",
                    fullName = "Rohit Negi",
                    age = 19,
                    phone = "+91 93210 98765",
                    batchName = "Alpha Commando",
                    targetExam = "Indian Army Soldier GD",
                    heightCm = 174.0f,
                    weightKg = 68.0f,
                    chestNormalCm = 81.0f,
                    chestExpandedCm = 86.5f,
                    bloodGroup = "B+",
                    medicalStatus = "SHAPE-1 (Fit)",
                    avatarTag = "commando_1",
                    notes = "Improving 1600m splits. Focus on pace management."
                )
            )
            cadetDao.insertCadets(initialCadets)

            val initialPlan = DailyWorkoutPlan(
                dateString = "Daily Routine & WOD",
                batchName = "All Batches",
                focusArea = "1600m Speed Simulation & 40-Mark Beam Conditioning",
                morningDrill = "05:15 AM - 15m warm-up + 1600m Official Stopwatch Run (Target < 5:30) + 60 Dand-Baithak + 40 Push-ups",
                eveningDrill = "05:00 PM - 5km Aerobic Pace + 6x Pull-up Max Sets (Aim for 10 Clean Beams) + Core Abs 5 Min",
                academicTarget = "Physical Recovery, Hydration & Tactical obstacle training drills",
                coachQuote = "Drill hard in peace so you bleed less in combat!"
            )
            dailyWorkoutPlanDao.insertPlan(initialPlan)

            val now = System.currentTimeMillis()
            val day = 86400000L
            val initialLogs = listOf(
                PhysicalLog(
                    cadetId = 1,
                    logDate = now - 2 * day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 318,
                    pushUpsCount = 48,
                    sitUpsCount = 52,
                    dandBaithakCount = 85,
                    pullUpsCount = 10,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Perfect timing! 60 + 40 = 100/100 Total PET Marks achieved."
                ),
                PhysicalLog(
                    cadetId = 1,
                    logDate = now,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 314,
                    pushUpsCount = 50,
                    sitUpsCount = 55,
                    dandBaithakCount = 90,
                    pullUpsCount = 10,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "PB time 5:14! Ready for physical rally."
                ),
                PhysicalLog(
                    cadetId = 2,
                    logDate = now - day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 328,
                    pushUpsCount = 44,
                    sitUpsCount = 48,
                    dandBaithakCount = 100,
                    pullUpsCount = 9,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Superb squat stamina. Group 1 confirmed. Work on 10th chin-up."
                ),
                PhysicalLog(
                    cadetId = 3,
                    logDate = now - day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 340,
                    pushUpsCount = 38,
                    sitUpsCount = 42,
                    dandBaithakCount = 70,
                    pullUpsCount = 8,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Group 2 (48 Marks). Shave off 10 seconds for Group 1."
                ),
                PhysicalLog(
                    cadetId = 4,
                    logDate = now - day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 332,
                    pushUpsCount = 45,
                    sitUpsCount = 50,
                    dandBaithakCount = 80,
                    pullUpsCount = 10,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Full marks in Beam (40/40)! Close to Group 1 run."
                ),
                PhysicalLog(
                    cadetId = 5,
                    logDate = now - day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 410,
                    pushUpsCount = 30,
                    sitUpsCount = 40,
                    dandBaithakCount = 65,
                    pullUpsCount = 7,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Passed CMP women standards comfortably. Excellent rhythm."
                ),
                PhysicalLog(
                    cadetId = 6,
                    logDate = now - day,
                    runDistanceMeters = 1600,
                    runTimeSeconds = 344,
                    pushUpsCount = 36,
                    sitUpsCount = 38,
                    dandBaithakCount = 60,
                    pullUpsCount = 7,
                    ditchJumpPass = true,
                    zigZagPass = true,
                    coachNotes = "Qualified Group 2. Needs stride pacing."
                )
            )
            physicalLogDao.insertLogs(initialLogs)
        }
    }
}
