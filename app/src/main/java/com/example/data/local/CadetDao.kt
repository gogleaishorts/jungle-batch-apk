package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Cadet
import kotlinx.coroutines.flow.Flow

@Dao
interface CadetDao {
    @Query("SELECT * FROM cadets ORDER BY fullName ASC")
    fun getAllCadets(): Flow<List<Cadet>>

    @Query("SELECT * FROM cadets WHERE id = :id")
    fun getCadetById(id: Long): Flow<Cadet?>

    @Query("SELECT * FROM cadets WHERE batchName = :batchName ORDER BY fullName ASC")
    fun getCadetsByBatch(batchName: String): Flow<List<Cadet>>

    @Query("SELECT COUNT(*) FROM cadets")
    suspend fun getCadetCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCadet(cadet: Cadet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCadets(cadets: List<Cadet>)

    @Update
    suspend fun updateCadet(cadet: Cadet)

    @Delete
    suspend fun deleteCadet(cadet: Cadet)
}
