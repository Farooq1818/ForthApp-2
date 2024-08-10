package com.example.forthapp_2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.forthapp_2.databinding.ActivityUploadVideoBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Upload_video : AppCompatActivity() {
    private lateinit var binding: ActivityUploadVideoBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadVideoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.videoView.isVisible = false

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")

        binding.videoBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "video/*"
            videoLauncher.launch(intent)
        }
    }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri ->
                    progressDialog.show()
                    val ref = FirebaseStorage.getInstance().reference.child(
                        "video/" + System.currentTimeMillis() + "." + getFileType(uri)
                    )
                    ref.putFile(uri).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { downloadUri ->
                            FirebaseDatabase.getInstance().reference.child("video").push()
                                .setValue(downloadUri.toString())
                            progressDialog.dismiss()
                            Toast.makeText(this, "Video Upload Successful 😘", Toast.LENGTH_SHORT)
                                .show()
                            binding.videoBtn.isVisible = false
                            binding.videoView.isVisible = true
                            binding.videoView.setVideoURI(downloadUri)
                            val mediaController = MediaController(this)
                            mediaController.setAnchorView(binding.videoView)
                            binding.videoView.setMediaController(mediaController)
                            binding.videoView.start()
                            binding.videoView.setOnCompletionListener {
                                ref.delete().addOnSuccessListener {
                                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                        .addOnProgressListener { taskSnapshot ->
                            val value = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                            progressDialog.setMessage("Upload $value%")
                        }
                }
            }
        }

    private fun getFileType(uri: Uri): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(r.getType(uri))
    }
}

