package com.example.notesappinternalstorage.model

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException

class NotesRepository(private val context: Context) {

    private val folderName = "notes"

    private val notesDir: File = File(context.filesDir, folderName)

    init {
        if (!notesDir.exists()) {
            notesDir.mkdirs()
        }
    }

    suspend fun saveNote(note: Note) {
        try {
            val file = File(notesDir, note.name)
            file.writeText(note.content)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun getAllNotes(): Flow<List<Note>> = flow {
        val notes = notesDir.listFiles()?.map { file ->
            Note(file.name, file.readText())
        } ?: emptyList()
        emit(notes)
    }

    fun deleteNote(fileName: String) {
        val file = File(notesDir, fileName)
        file.delete()
    }
}