package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.DailyWorkoutPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWorkoutPlanDao {
    @Query("SELECT * FROM daily_workout_plans ORDER BY id DESC LIMIT 1")
    fun getLatestPlan(): Flow<DailyWorkoutPlan?>

    @Query("SELECT * FROM daily_workout_plans ORDER BY id DESC")
    fun getAllPlans(): Flow<List<DailyWorkoutPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: DailyWorkoutPlan): Long

    @Query("SELECT COUNT(*) FROM daily_workout_plans")
    suspend fun getPlanCount(): Int
}
