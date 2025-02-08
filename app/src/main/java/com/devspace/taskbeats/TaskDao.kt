package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(taskEntities: List<TaskEntity>)

    @Query("SELECT * FROM taskentity")
    fun getAll(): List<TaskEntity>
}