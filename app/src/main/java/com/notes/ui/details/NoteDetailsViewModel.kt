package com.notes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NotesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NoteDetailsViewModel @AssistedInject constructor(
    private val notesRepository: NotesRepository,
    @Assisted private var noteId: Long?
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(noteId: Long?): NoteDetailsViewModel
    }

    private val _note = MutableLiveData<NoteItem>()
    val note: LiveData<NoteItem> = _note

    private var noteItem = NoteItem()

    init {
        if (noteId != null)
            viewModelScope.launch {
                noteItem = notesRepository.loadNoteById(noteId!!)
                _note.postValue(noteItem)
            }
    }

    fun deleteNote() {
        if (noteId != null) {
            viewModelScope.launch {
                notesRepository.deleteNote(noteItem)
            }
        }
    }

    fun saveNote(title: String, content: String) {
        noteItem.title = title
        noteItem.content = content

        if (noteId != null) {
            viewModelScope.launch {
                notesRepository.updateNote(noteItem)
            }
        } else {
            viewModelScope.launch {
                notesRepository.createNote(noteItem)
            }
        }
    }
}

data class NoteItem(
    var id: Long = 0,
    var title: String = "",
    var content: String = "",
    var createdAt: LocalDateTime = LocalDateTime.MIN,
)