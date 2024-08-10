package com.example.forthapp_2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.forthapp_2.databinding.ActivityNotesPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotesPage : AppCompatActivity() {
    private val binding: ActivityNotesPageBinding by lazy {
        ActivityNotesPageBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.saveBtn.setOnClickListener {
            val title = binding.titleBox.text.toString()
            val description = binding.decsBox.text.toString()
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Fill all boxes", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val notesPageKey = databaseReference.child("users").child(user.uid).child("notes").push().key
                    val noteItem = NoteDataClass(title, description, notesPageKey ?: "")
                    if (notesPageKey != null) {
                        databaseReference.child("users").child(user.uid).child("notes").child(notesPageKey).setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }
}

