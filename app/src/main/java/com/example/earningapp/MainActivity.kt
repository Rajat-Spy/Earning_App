package com.example.earningapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.databinding.ActivityMainBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signUp.setOnClickListener {
            if(binding.userName.text.toString().equals("") || binding.userAge.text.toString().equals("") || binding.userEmail.text.toString().equals("") ||binding.userPassword.text.toString().equals(""))
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            else{
                Firebase.auth.createUserWithEmailAndPassword(binding.userEmail.text.toString(), binding.userPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        var user = User(binding.userName.text.toString(), binding.userAge.text.toString().toInt(), binding.userEmail.text.toString(), binding.userPassword.text.toString())
                        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).push().setValue(user).addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }

                    }else {
                        Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser!= null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}