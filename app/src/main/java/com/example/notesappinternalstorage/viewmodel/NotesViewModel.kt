package com.example.notesappinternalstorage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappinternalstorage.model.Note
import com.example.notesappinternalstorage.model.NotesRepository
import kotlinx.coroutines.launch
/**
 * ViewModel class responsible for managing UI-related data in a lifecycle-conscious way.
 * It interacts with the [NotesRepository] to fetch, save, and delete notes.
 * It exposes a [LiveData] of notes for the UI to observe.
 */
class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    // LiveData holding the list of notes to be observed by the UI
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    init {
        // Load notes when the ViewModel is initialized
        loadNotes()

    }

    /**
     * Loads all the notes from the repository and updates the [LiveData].
     * This function is called during initialization to populate the notes in the UI.
     */
    private fun loadNotes() {
        // Perform loading in the ViewModel's coroutine scope to avoid blocking the main thread
        viewModelScope.launch {
            val noteList = repository.getAllNotes()
            _notes.value = noteList
        }
    }

    /**
     * Saves a new note to the repository and refreshes the list of notes.
     * This function is suspended because it involves file operations that must be performed asynchronously.
     *
     * @param note The note to save.
     */
    suspend fun saveNote(note: Note) {
        // Attempt to save the note, and reload the notes if successful
            if (repository.saveNote(note)) {
                loadNotes()
        }
    }

    /**
     * Deletes a note by its file name and refreshes the list of notes.
     * This function is called on a background thread to avoid blocking the UI thread.
     *
     * @param fileName The name of the note to delete.
     */
    fun deleteNote(fileName: String) {
        // Perform delete in the ViewModel's coroutine scope
        viewModelScope.launch {
            if (repository.deleteNote(fileName)) {
                loadNotes()
            }
        }
    }
}