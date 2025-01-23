package com.example.notesappinternalstorage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappinternalstorage.model.Note
import com.example.notesappinternalstorage.model.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collect { noteList ->
                _notes.value = noteList
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (repository.saveNote(note)) {
                loadNotes()
            }
        }
    }

    fun deleteNote(fileName: String) {
        viewModelScope.launch {
            if (repository.deleteNote(fileName)) {
                loadNotes()
            }
        }
    }
}