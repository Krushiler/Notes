package com.notes.data

import com.notes.ui.details.NoteItem
import com.notes.ui.list.NoteListItem
import com.notes.util.currentLocalDateTime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDatabase: NoteDatabase,
    private val appCoroutineScope: CoroutineScope
) {

    suspend fun loadNotes() = withContext(Dispatchers.IO) {
        noteDatabase.noteDao().getAll().mapLatest {
            it.map { item ->
                NoteListItem(
                    id = item.id,
                    title = item.title,
                    content = item.content,
                )
            }
        }
    }

    suspend fun loadNoteById(id: Long) = withContext(Dispatchers.IO) {
        val note = noteDatabase.noteDao().getNoteById(id)
        return@withContext NoteItem(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
        )

    }

    suspend fun updateNote(noteItem: NoteItem) = withContext(Dispatchers.IO) {
        appCoroutineScope.launch {
            noteDatabase.noteDao().updateAll(NoteDbo(
                id = noteItem.id,
                title = noteItem.title,
                content = noteItem.content,
                createdAt = noteItem.createdAt,
                modifiedAt = currentLocalDateTime()
            ))
        }
    }

    suspend fun createNote(noteItem: NoteItem) = withContext(Dispatchers.IO) {
        appCoroutineScope.launch {
            noteDatabase.noteDao().insertAll(NoteDbo(
                title = noteItem.title,
                content = noteItem.content,
                createdAt = currentLocalDateTime(),
                modifiedAt = currentLocalDateTime()
            ))
        }
    }

    suspend fun deleteNote(noteItem: NoteItem) = withContext(Dispatchers.IO) {
        appCoroutineScope.launch {
            noteDatabase.noteDao().deleteById(noteItem.id)
        }
    }
}
