package com.example.earningapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    var isExpand = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.imageButton.setOnClickListener {
            if(isExpand)
            {
                binding.expandableConstraintLayout.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.up_arrow)
            }
            else{
                binding.expandableConstraintLayout.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)
            }
            isExpand =!isExpand
        }
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.getValue<User>()
//                    Log.d("MYTAG", "onDataChange: $snapshot")
                    binding.name.text = user?.name
                    binding.nameppr.text = user?.name
                    binding.password.text = user?.password
                    binding.email.text = user?.email
                    binding.age.text = user?.age.toString()
               }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
    }
}