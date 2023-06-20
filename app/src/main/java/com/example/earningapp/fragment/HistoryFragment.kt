package com.example.earningapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earningapp.R
import com.example.earningapp.Withdrawl
import com.example.earningapp.adapter.HistoryAdapter
import com.example.earningapp.adapter.categoryAdapter
import com.example.earningapp.databinding.FragmentHistoryBinding
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
import java.util.Collections

class HistoryFragment : Fragment() {
    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: HistoryAdapter
    private var listHistory = ArrayList<HistoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listHistory.clear()
                 var listHistory1 = ArrayList<HistoryModelClass>()
                for(dataSnapshot in snapshot.children){
                    var data = dataSnapshot.getValue(HistoryModelClass::class.java)
                    listHistory1.add(data!!)
                    adapter.notifyDataSetChanged()

                }
                listHistory1.reverse()
                listHistory.addAll(listHistory1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.HistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(listHistory)
        binding.HistoryRecyclerView.adapter = adapter
        binding.HistoryRecyclerView.setHasFixedSize(true)
        // Inflate the layout for this fragment
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
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var currentCoin = snapshot.getValue() as Long
                    binding.coin.text = currentCoin.toString()
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