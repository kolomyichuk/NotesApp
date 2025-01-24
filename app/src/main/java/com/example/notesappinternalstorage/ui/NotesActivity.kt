package com.example.notesappinternalstorage.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappinternalstorage.databinding.ActivityNotesBinding
import com.example.notesappinternalstorage.model.Note
import com.example.notesappinternalstorage.model.NotesRepository
import com.example.notesappinternalstorage.viewmodel.NotesViewModel
import com.example.notesappinternalstorage.viewmodel.NotesViewModelFactory
import kotlinx.coroutines.launch

/**
 * Activity that displays a list of notes and provides functionality for adding, editing, and deleting notes.
 * This activity observes the [NotesViewModel] to get updates to the list of notes.
 */
class NotesActivity : AppCompatActivity() {
    // View binding for the activity layout
    private lateinit var binding: ActivityNotesBinding

    // ViewModel to manage the note data
    private lateinit var viewModel: NotesViewModel

    // Adapter to display the notes in the RecyclerView
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository and ViewModel
        val repository = NotesRepository(this)
        val factory = NotesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NotesViewModel::class.java)

        // Initialize the adapter and set click listeners for editing and deleting notes
        adapter = NotesAdapter(
            onClickEditNote = { note -> showEditNoteDialog(note) },
            onClickDeleteNote = { note -> viewModel.deleteNote(note.name) }
        )

        // Set up the RecyclerView with a LinearLayoutManager and adapter
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        // Observe the notes LiveData from the ViewModel and update the adapter when data changes
        viewModel.notes.observe(this) { notes ->
            adapter.submitList(notes)
        }

        // Set click listener for the "Add Note" button
        binding.btnAdd.setOnClickListener {
            showAddNoteDialog()
        }
    }

    /**
     * Show a dialog to add a new note.
     * The user can enter the content of the note and save it.
     */
    private fun showAddNoteDialog() {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this).apply { hint = "Enter the text of the note" }
        dialog.setTitle("New note")
        dialog.setView(input)

        dialog.setPositiveButton("Save") { _, _ ->
            val content = input.text.toString()
            if (content.isNotEmpty()) {
                // Create a new note and save it via the ViewModel
                lifecycleScope.launch {
                    val note = Note("note_${System.currentTimeMillis()}.txt", content)
                    viewModel.saveNote(note)
                }
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    /**
     * Show a dialog to edit an existing note.
     * The user can update the content of the note and save the changes.
     *
     * @param note The note to be edited.
     */
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
                // Create a copy of the note with the updated content and save it via the ViewModel
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