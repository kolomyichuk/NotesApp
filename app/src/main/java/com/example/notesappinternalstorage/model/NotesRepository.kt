package com.example.notesappinternalstorage.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**  The NotesRepository is responsible for managing note data
 * within the app by interacting with the internal storage of the device.
 * It provides functions to save, retrieve, and delete notes.
 * The repository ensures all file operations are performed asynchronously
on a background thread to prevent blocking the main UI thread. */

class NotesRepository(context: Context) {

    private val folderName = "notes"

    // Directory for storing notes in internal storage
    private val notesDir: File = File(context.filesDir, folderName)

    /**
     * Initializes the NotesRepository by checking if the notes directory exists.
     * If it doesn't, it creates the directory to ensure it is available for storing notes.
     */
    init {
        if (!notesDir.exists()) {
            notesDir.mkdirs()
        }
    }

    /**
     * Saves a note to internal storage.
     * This function runs asynchronously on a background thread to avoid blocking the main UI thread.
     * It writes the note content to a file named after the note's name.
     *
     * @param note The note object to be saved.
     * @return Boolean indicating whether the note was successfully saved.
     */
    suspend fun saveNote(note: Note): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(notesDir, note.name)
                file.writeText(note.content)
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }
    /**
     * Retrieves all notes from the internal storage directory.
     * This function asynchronously loads the files and converts them into a list of Note objects.
     *
     * @return A list of notes retrieved from the internal storage.
     */
    suspend fun getAllNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            val notes = notesDir.listFiles()?.map { file ->
                Note(file.name, file.readText())
            } ?: emptyList()
            notes
        }
    }

    /**
     * Deletes a note from the internal storage.
     * This function removes the note's file from the directory.
     *
     * @param fileName The name of the note file to be deleted.
     * @return Boolean indicating whether the note was successfully deleted.
     */
    fun deleteNote(fileName: String): Boolean {
        val file = File(notesDir, fileName)
        return file.delete()
    }
}