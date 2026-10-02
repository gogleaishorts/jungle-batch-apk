package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_workout_plans")
data class DailyWorkoutPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateString: String, // e.g. "Today's Drill Schedule"
    val batchName: String, // "All Batches" or "Alpha Commando"
    val focusArea: String, // "1600m Pace & Beam Power"
    val morningDrill: String, // "05:15 AM - 10 Min Warm-up + 4x400m Intervals + 60 Dand-Baithak"
    val eveningDrill: String, // "05:00 PM - 5km Endurance Jog + Pull-up Pyramid (5-8-10-8-5) + Core Abs"
    val academicTarget: String, // "Science Chapter 4 (Mechanics) + 30 General Knowledge PyQs"
    val coachQuote: String = "Discipline is the bridge between goals and military accomplishment."
)
