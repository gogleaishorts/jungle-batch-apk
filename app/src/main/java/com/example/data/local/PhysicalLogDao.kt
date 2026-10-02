package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PhysicalLog
import kotlinx.coroutines.flow.Flow

@Dao
interface PhysicalLogDao {
    @Query("SELECT * FROM physical_logs ORDER BY logDate DESC")
    fun getAllLogs(): Flow<List<PhysicalLog>>

    @Query("SELECT * FROM physical_logs WHERE cadetId = :cadetId ORDER BY logDate DESC")
    fun getLogsForCadet(cadetId: Long): Flow<List<PhysicalLog>>

    @Query("SELECT COUNT(*) FROM physical_logs")
    suspend fun getLogsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PhysicalLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<PhysicalLog>)

    @Delete
    suspend fun deleteLog(log: PhysicalLog)
}
