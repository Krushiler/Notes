package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NotesRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NoteListViewModel @AssistedInject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(): NoteListViewModel
    }

    private val _notes = MutableLiveData<List<NoteListItem>?>()

    val notes: LiveData<List<NoteListItem>?> = _notes

    init {
        viewModelScope.launch {
            notesRepository.loadNotes().collect {
                _notes.postValue(it)
            }

        }
    }

}

data class NoteListItem(
    val id: Long,
    val title: String,
    val content: String,
)
