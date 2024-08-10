package com.example.forthapp_2

import android.app.Activity
import android.content.ContentProviderClient
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.forthapp_2.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
//        val db = FirebaseFirestore.getInstance()

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.googleicon.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                // Register user with Firebase
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                            // Save user data to Firestore
//                            val user = hashMapOf(
//                                "email" to email,
//                                "password" to password
//                            )
//                            db.collection("users")
//                                .add(user)
//                                .addOnSuccessListener { documentReference ->
//                                    Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                                }


                            startActivity(Intent(this, LogInActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result ->
        if(result.resultCode==Activity.RESULT_OK)
        {
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful)
            {
                val account:GoogleSignInAccount?=task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "SuccessFull ☺️", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Failed 😢", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }else{
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }
}


