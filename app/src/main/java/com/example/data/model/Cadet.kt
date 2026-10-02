package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cadets")
data class Cadet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rollNo: String,
    val fullName: String,
    val age: Int = 20,
    val phone: String = "+91 98765 00000",
    val batchName: String = "Alpha Commando",
    val targetExam: String = "Indian Army Soldier GD",
    val heightCm: Float = 172.0f,
    val weightKg: Float = 68.0f,
    val chestNormalCm: Float = 80.0f,
    val chestExpandedCm: Float = 86.0f,
    val bloodGroup: String = "B+",
    val medicalStatus: String = "SHAPE-1 (Fit)",
    val avatarTag: String = "commando_1",
    val photoUri: String? = null,
    val admissionDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val notes: String = ""
) {
    val chestExpansionCm: Float
        get() = (chestExpandedCm - chestNormalCm).coerceAtLeast(0f)

    val bmi: Float
        get() {
            val heightM = heightCm / 100f
            return if (heightM > 0) weightKg / (heightM * heightM) else 0f
        }

    val isPhysicalStandardFit: Boolean
        get() = heightCm >= 168f && chestExpansionCm >= 5.0f && bmi in 18.5f..25.5f
}
