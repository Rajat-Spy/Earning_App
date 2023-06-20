package com.example.earningapp.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.R
import com.example.earningapp.Withdrawl
import com.example.earningapp.databinding.FragmentSpinBinding
import com.example.earningapp.model.HistoryModelClass
import com.example.earningapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.sql.Time
import kotlin.time.Duration.Companion.microseconds

class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100", "Try Again", "200", "Try Again", "500", "Try Again")
    var currentChance = 0L
    var currentCoin = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinBinding.inflate(inflater, container, false)
        binding.coinImage.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.coin.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.getValue<User>()
//                    Log.d("MYTAG", "onDataChange: $snapshot")
                    binding.name.text = user?.name

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    currentChance = snapshot.getValue() as Long
                    binding.chance.text = (snapshot.getValue() as Long).toString()
                }
                else{
                    var temp = 0
                    binding.chance.text =temp.toString()
                    binding.spinbtn.isEnabled = false
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    currentCoin = snapshot.getValue() as Long
                    binding.coin.text = currentCoin.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return binding.root
    }
    private fun showResult(itemTitle: String, spin: Int) {
        if(spin%2 == 0){
            var winCoins = itemTitle.toInt()
            Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).setValue(winCoins+currentCoin)
            var historyModel = HistoryModelClass(System.currentTimeMillis().toString(), winCoins.toString(), false)
            Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).push().setValue(historyModel)
            binding.coin.text = (winCoins+currentCoin).toString()
        }
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        currentChance -= 1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.spinbtn.isEnabled = true   //Enable the button again
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinbtn.setOnClickListener {
            binding.spinbtn.isEnabled = false //Disable the button while the wheel is spinning
            if(currentChance > 0) {
                val spin =
                    java.util.Random().nextInt(6)    //Generate a random value between 0 and 5
                val degrees =
                    60f * spin   //Calculate the rotation degrees based on the random value

                timer = object : CountDownTimer(5000, 50) {
                    var rotation = 0f
                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f
                        if (rotation >= degrees) {
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitles[spin], spin)
                        }
                        binding.wheel.rotation = rotation
                    }

                    override fun onFinish() {}
                }.start()
            }else {
                Toast.makeText(requireContext(), "Oops you dont have chances left", Toast.LENGTH_SHORT).show()
            }
        }
    }
}