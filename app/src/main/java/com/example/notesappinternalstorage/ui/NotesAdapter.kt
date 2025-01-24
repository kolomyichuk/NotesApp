package com.example.notesappinternalstorage.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappinternalstorage.R
import com.example.notesappinternalstorage.model.Note
/**
 * Adapter for displaying a list of notes in a RecyclerView.
 * Handles note editing and deletion actions through callback functions.
 */
class NotesAdapter(
    private val onClickEditNote: (Note) -> Unit,
    private val onClickDeleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    // List holding the notes to be displayed in the RecyclerView
    private val notes = mutableListOf<Note>()

    /**
     * Updates the list of notes in the adapter.
     * Clears the existing list and adds the new list of notes, then notifies the adapter to refresh.
     *
     * @param newNotes The new list of notes to display.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder that binds the note data to the views in the item layout.
     */
    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // UI elements in the item layout
        private val contentTextView = itemView.findViewById<TextView>(R.id.tvNote)
        private val btnEditNote = itemView.findViewById<ImageView>(R.id.btnEdit)
        private val btnDeleteNote = itemView.findViewById<ImageView>(R.id.btnDelete)

        /**
         * Binds the note data to the views in the item layout.
         * Sets click listeners for editing and deleting the note.
         *
         * @param note The note to bind to the views.
         */
        fun bind(note: Note) {
            contentTextView.text = note.content
            btnEditNote.setOnClickListener { onClickEditNote(note) }
            btnDeleteNote.setOnClickListener { onClickDeleteNote(note) }
        }
    }

    /**
     * Creates a new ViewHolder by inflating the item layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type for the item.
     * @return A new ViewHolder for the note item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    /**
     * Returns the total number of notes in the adapter.
     *
     * @return The number of notes.
     */
    override fun getItemCount(): Int {
        return notes.size
    }

    /**
     * Binds the note data to the ViewHolder for the given position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }
}