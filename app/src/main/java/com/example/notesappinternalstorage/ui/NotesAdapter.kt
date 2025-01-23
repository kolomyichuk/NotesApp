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

class NotesAdapter(
    private val onClickEditNote: (Note) -> Unit,
    private val onClickDeleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private val notes = mutableListOf<Note>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTextView = itemView.findViewById<TextView>(R.id.tvNote)
        private val btnEditNote = itemView.findViewById<ImageView>(R.id.btnEdit)
        private val btnDeleteNote = itemView.findViewById<ImageView>(R.id.btnDelete)

        fun bind(note: Note) {
            contentTextView.text = note.content
            btnEditNote.setOnClickListener { onClickEditNote(note) }
            btnDeleteNote.setOnClickListener { onClickDeleteNote(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }
}