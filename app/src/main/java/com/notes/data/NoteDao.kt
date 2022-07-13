package com.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteDbo>>

    @Query("SELECT * FROM notes WHERE id=:id")
    fun getNoteById(id: Long): NoteDbo

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Update
    fun updateAll(vararg notes: NoteDbo)

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteById(id: Long)
}