package com.example.forthapp_2

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentResolver.MimeTypeInfo
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.forthapp_2.databinding.ActivityUploadPhotoBinding
import com.google.android.play.integrity.internal.r
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class Upload_Photo : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.uploadInnerPhotoBtn.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_PICK
            intent.type="image/*"
            imageLauncher.launch(intent)
        }
    }
    val imageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK)
        {
            if (it.data!= null){
                val ref=Firebase.storage.reference.child("photo/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Firebase.database.reference.child("Photo").push().setValue(it.toString())
                        binding.uploadInnerPhoto.setImageURI(it)
                        Toast.makeText(this, "Photo Upload Successfull😘", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(it.toString()).into(binding.uploadInnerPhoto);
                    }
                }
            }
        }
    }

    private fun getFileType(data: Uri?): Any? {
        val r=contentResolver
        val mimeType=MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data!!))
    }
}

