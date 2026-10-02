package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FieldConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldConfigDao {
    @Query("SELECT * FROM field_configs WHERE id = 1 LIMIT 1")
    fun getFieldConfig(): Flow<FieldConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFieldConfig(config: FieldConfig)

    @Update
    suspend fun updateFieldConfig(config: FieldConfig)
}
