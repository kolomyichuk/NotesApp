package com.example.notesappinternalstorage.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappinternalstorage.databinding.ActivityNoteBinding
import com.example.notesappinternalstorage.model.Note
import com.example.notesappinternalstorage.model.NotesRepository
import com.example.notesappinternalstorage.viewmodel.NotesViewModel
import com.example.notesappinternalstorage.viewmodel.NotesViewModelFactory
import kotlinx.coroutines.launch

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NotesRepository(this)
        val factory = NotesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)

        adapter = NotesAdapter(
            onClickEditNote = { note -> showEditNoteDialog(note) },
            onClickDeleteNote = { note -> viewModel.deleteNote(note.name) }
        )

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        viewModel.notes.observe(this) { notes ->
            adapter.submitList(notes)
        }

        binding.btnSave.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this).apply { hint = "Enter the text of the note" }
        dialog.setTitle("New note")
        dialog.setView(input)

        dialog.setPositiveButton("Save") { _, _ ->
            val content = input.text.toString()
            if (content.isNotEmpty()) {
                lifecycleScope.launch {
                    val note = Note("note_${System.currentTimeMillis()}.txt", content)
                    viewModel.saveNote(note)
                }
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    private fun showEditNoteDialog(note: Note) {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this).apply {
            setText(note.content)
        }
        dialog.setTitle("Edit note")
        dialog.setView(input)

        dialog.setPositiveButton("Update") { _, _ ->
            val updateContent = input.text.toString()
            if (updateContent.isNotEmpty()) {
                lifecycleScope.launch {
                    val updateNote = note.copy(content = updateContent)
                    viewModel.saveNote(updateNote)
                }
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }
}