package com.example.forthapp_2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forthapp_2.databinding.ActivityOtherAppDtaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class OtherAppDta : AppCompatActivity() {
    private lateinit var binding: ActivityOtherAppDtaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityOtherAppDtaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        var list= arrayListOf<User>()
        var rvAdapter=Rv_Adapter(this,list)
        binding.rv.layoutManager=LinearLayoutManager(this)
        binding.rv.adapter=rvAdapter
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                list.clear()
                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
                    var user=document.toObject(User::class.java)
                    user.id=document.id
                    list.add(user)
                }
                rvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}