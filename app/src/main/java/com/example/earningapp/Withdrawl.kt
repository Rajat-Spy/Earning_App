package com.example.earningapp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.earningapp.databinding.FragmentWithdrawlBinding
import com.example.earningapp.model.HistoryModelClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Withdrawl : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWithdrawlBinding
    var currentCoin = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithdrawlBinding.inflate(inflater, container, false)
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    currentCoin = snapshot.getValue() as Long
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.transfer.setOnClickListener {
            if(binding.amount.text.toString().toDouble() <= currentCoin){
                Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).setValue(currentCoin-binding.amount.text.toString().toDouble())
                var historyModel = HistoryModelClass(System.currentTimeMillis().toString(), binding.amount.text.toString(), true)
                Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).push().setValue(historyModel)
            }
            else{
                Toast.makeText(activity, "Oops you dont have enough coins in your Account", Toast.LENGTH_SHORT).show()
            }

        }
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var currentCoin = snapshot.getValue() as Long
                    binding.currentCoins.text = currentCoin.toString()
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return binding.root
    }

    companion object {

    }
}