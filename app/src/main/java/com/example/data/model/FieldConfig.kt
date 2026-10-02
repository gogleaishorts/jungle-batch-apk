package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_configs")
data class FieldConfig(
    @PrimaryKey
    val id: Int = 1,
    val personLabel: String = "Cadet",
    val idLabel: String = "Roll No",
    val groupLabel: String = "Batch",
    val categoryLabel: String = "Target Exam",
    val metricRunLabel: String = "1600m Run Timing",
    val metricPullUpsLabel: String = "Pull-ups (Beam)",
    val metricPushUpsLabel: String = "Push-ups",
    val metricDandBaithakLabel: String = "Dand-Baithak (Squats)",
    val metricSitUpsLabel: String = "Sit-ups",
    val qualifier1Label: String = "9-ft Ditch Jump",
    val qualifier2Label: String = "Zig-zag Balance",
    val availableBatches: String = "Alpha Commando, Bravo Agnipath, Charlie SSC-GD, Delta NDA/CDS"
) {
    val batchList: List<String>
        get() = availableBatches.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
