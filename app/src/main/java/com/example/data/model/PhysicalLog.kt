package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "physical_logs",
    foreignKeys = [
        ForeignKey(
            entity = Cadet::class,
            parentColumns = ["id"],
            childColumns = ["cadetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("cadetId"), Index("logDate")]
)
data class PhysicalLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cadetId: Long,
    val logDate: Long = System.currentTimeMillis(),
    val runDistanceMeters: Int = 1600,
    val runTimeSeconds: Int = 340, // e.g. 5 min 40 sec
    val pushUpsCount: Int = 35,
    val sitUpsCount: Int = 40,
    val dandBaithakCount: Int = 60, // Desi squats
    val pullUpsCount: Int = 9,      // Beam / Chin-ups
    val ditchJumpPass: Boolean = true,
    val zigZagPass: Boolean = true,
    val coachNotes: String = ""
) {
    // Standard Indian Army PET running evaluation
    val runPetGroup: String
        get() = when {
            runTimeSeconds <= 330 -> "Group 1 (60 Marks)" // <= 5:30
            runTimeSeconds <= 345 -> "Group 2 (48 Marks)" // 5:31 to 5:45
            else -> "Not Qualified (> 5:45)"
        }

    val runMarks: Int
        get() = when {
            runTimeSeconds <= 330 -> 60
            runTimeSeconds <= 345 -> 48
            else -> 0
        }

    // Standard Indian Army Beam (Pull-ups) marks out of 40
    val pullUpsMarks: Int
        get() = when {
            pullUpsCount >= 10 -> 40
            pullUpsCount == 9 -> 33
            pullUpsCount == 8 -> 27
            pullUpsCount == 7 -> 21
            pullUpsCount == 6 -> 16
            else -> 0
        }

    val totalPetMarks: Int
        get() = runMarks + pullUpsMarks

    val isAllQualifiersPassed: Boolean
        get() = ditchJumpPass && zigZagPass && pullUpsCount >= 6 && runMarks > 0

    val formattedRunTime: String
        get() {
            val minutes = runTimeSeconds / 60
            val seconds = runTimeSeconds % 60
            return String.format("%d:%02d", minutes, seconds)
        }
}
