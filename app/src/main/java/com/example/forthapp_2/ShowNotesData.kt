package com.example.forthapp_2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forthapp_2.databinding.ActivityShowNotesDataBinding
import com.example.forthapp_2.databinding.UpdateNotesPopBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowNotesData : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    private val binding: ActivityShowNotesDataBinding by lazy {
        ActivityShowNotesDataBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        recyclerView = binding.recycleViewNotes
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteDataClass>()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteDataClass::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    val adapter = NoteAdapter(noteList, this@ShowNotesData)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes").child(noteId)
            noteReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onUpdateClick(noteId: String, title: String, description: String) {
        val dialogBinding = UpdateNotesPopBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.updateTitlePop.setText(title)
        dialogBinding.updateDescriptionPop.setText(description)

        dialogBinding.updateSaveBtn.setOnClickListener {
            val newTitle = dialogBinding.updateTitlePop.text.toString()
            val newDescription = dialogBinding.updateDescriptionPop.text.toString()
            if (newTitle.isNotEmpty() && newDescription.isNotEmpty()) {
                updateNoteInDatabase(noteId, newTitle, newDescription)
            } else {
                Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialogBinding.updateCancleBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateNoteInDatabase(noteId: String, title: String, description: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes").child(noteId)
            val updatedNote = NoteDataClass(title, description, noteId)
            noteReference.setValue(updatedNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}


