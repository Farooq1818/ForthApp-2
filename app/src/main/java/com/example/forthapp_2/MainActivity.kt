package com.example.forthapp_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.forthapp_2.databinding.ActivityMainBinding
import com.example.forthapp_2.databinding.ActivityNotesPageBinding
import com.example.forthapp_2.databinding.ActivityShowNotesDataBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.writeBtn.setOnClickListener{
            startActivity(Intent(this, NotesPage::class.java))
        }
        binding.ShowAppDtaBtn.setOnClickListener{
            startActivity(Intent(this, OtherAppDta::class.java))
        }
        binding.deleteBtn.setOnClickListener{
            startActivity(Intent(this, ShowNotesData::class.java))
        }
        binding.uploadPhotoBtn.setOnClickListener{
            startActivity(Intent(this, Upload_Photo::class.java))
        }
        binding.Uploadvideobtn.setOnClickListener{
            startActivity(Intent(this, Upload_video::class.java))
        }
    }
}



