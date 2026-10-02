package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.Cadet
import com.example.data.model.DailyWorkoutPlan
import com.example.data.model.FieldConfig
import com.example.data.model.PhysicalLog

@Database(
    entities = [
        Cadet::class,
        PhysicalLog::class,
        DailyWorkoutPlan::class,
        FieldConfig::class
    ],
    version = 3,
    exportSchema = false
)
abstract class JungleBatchDatabase : RoomDatabase() {
    abstract fun cadetDao(): CadetDao
    abstract fun physicalLogDao(): PhysicalLogDao
    abstract fun dailyWorkoutPlanDao(): DailyWorkoutPlanDao
    abstract fun fieldConfigDao(): FieldConfigDao

    companion object {
        @Volatile
        private var INSTANCE: JungleBatchDatabase? = null

        fun getDatabase(context: Context): JungleBatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JungleBatchDatabase::class.java,
                    "jungle_batch_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
