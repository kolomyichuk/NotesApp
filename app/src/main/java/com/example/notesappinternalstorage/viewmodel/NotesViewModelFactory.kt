package com.example.notesappinternalstorage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesappinternalstorage.model.NotesRepository

/**
 * Factory class for creating instances of the [NotesViewModel].
 * This is required because [NotesViewModel] has a constructor that takes a parameter
 * ([NotesRepository]), and ViewModelProvider needs to know how to instantiate it.
 */

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the [NotesViewModel] class.
     * This function checks if the requested ViewModel class is [NotesViewModel], and if so,
     * it returns a new instance of [NotesViewModel] initialized with the provided repository.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A new instance of [NotesViewModel].
     * @throws IllegalArgumentException if the ViewModel class is unknown or not [NotesViewModel].
     */

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is NotesViewModel
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)){
            // Suppress the unchecked cast warning because we know the class is NotesViewModel
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
        // Throw an exception if the ViewModel class is not recognized
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}