package com.example.notesappinternalstorage.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class NotesRepository(context: Context) {

    private val folderName = "notes"

    private val notesDir: File = File(context.filesDir, folderName)

    init {
        if (!notesDir.exists()) {
            notesDir.mkdirs()
        }
    }

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

    suspend fun getAllNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            val notes = notesDir.listFiles()?.map { file ->
                Note(file.name, file.readText())
            } ?: emptyList()
            notes
        }
    }

    fun deleteNote(fileName: String): Boolean {
        val file = File(notesDir, fileName)
        return file.delete()
    }
}