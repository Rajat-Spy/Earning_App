package com.example.earningapp.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentSpinBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100", "Try Again", "200", "Try Again", "500", "Try Again")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinBinding.inflate(inflater, container, false)
//        binding.coinImage.setOnClickListener{
//            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
//            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
//            bottomSheetDialog.enterTransition
//        }
//        binding.coin.setOnClickListener{
//            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
//            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
//            bottomSheetDialog.enterTransition
//        }
//        Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var user = snapshot.getValue<User>()
////                    Log.d("MYTAG", "onDataChange: $snapshot")
//                    binding.name.text = user?.name
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            }
//        )
        return binding.root
    }
    private fun showResult(itemTitle: String) {
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        binding.spinbtn.isEnabled = true   //Enable the button again
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinbtn.setOnClickListener {
            binding.spinbtn.isEnabled = false   //Disable the button while the wheel is spinning
            val spin = java.util.Random().nextInt(6)    //Generate a random value between 0 and 5
            val degrees = 60f*spin   //Calculate the rotation degrees based on the random value

            timer = object : CountDownTimer(5000, 50) {
                var rotation = 0f
                override fun onTick(millisUntilFinished: Long) {
                    rotation += 5f
                    if(rotation >= degrees) {
                        rotation = degrees
                        timer.cancel()
                        showResult(itemTitles[spin])
                    }
                    binding.wheel.rotation = rotation
                }

                override fun onFinish() {}
            }.start()
        }
    }
}