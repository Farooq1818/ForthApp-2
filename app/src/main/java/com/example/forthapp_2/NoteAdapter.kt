package com.example.forthapp_2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forthapp_2.databinding.NotesiteamBinding

class NoteAdapter(private val notes: List<NoteDataClass>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteClick(noteId: String)
        fun onUpdateClick(noteId: String, title: String, description: String)
    }

    class NoteViewHolder(internal val binding: NotesiteamBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteDataClass) {
            binding.titleiteam.text = note.title
            binding.Decriptioniteam.text = note.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesiteamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)

        holder.binding.updateBtn.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId, note.title, note.description)
        }

        holder.binding.deleteBtn.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }
    }
}



