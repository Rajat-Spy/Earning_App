package com.example.earningapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.example.earningapp.databinding.ActivityQuizBinding
import com.example.earningapp.model.QuestionModel
import com.example.earningapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private lateinit var questionList: ArrayList<QuestionModel>
    var currentQuestion = 0
    var score = 0
    var currentChance = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    currentChance = snapshot.value as Long
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        questionList = ArrayList<QuestionModel>()
        var image = intent.getIntExtra("categoryimg", 0)
        var catText = intent.getStringExtra("questionType")
        Firebase.firestore.collection("Questions").document(catText.toString()).collection("question1").get().addOnSuccessListener {
            questionData ->
            questionList.clear()
            for(data in questionData.documents){
                var question: QuestionModel? = data.toObject(QuestionModel::class.java)
                questionList.add(question!!)
            }
            binding.question.text = questionList.get(currentQuestion).question
            binding.option1.text = questionList.get(currentQuestion).option1
            binding.option2.text = questionList.get(currentQuestion).option2
            binding.option3.text = questionList.get(currentQuestion).option3
            binding.option4.text = questionList.get(currentQuestion).option4
        }
        binding.categoryimg.setImageResource(image)
        binding.coinImage.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        binding.coin.setOnClickListener{
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawl()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager, "TEST")
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
        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }
    }

    private fun nextQuestionAndScoreUpdate(s: String) {
        if(s == questionList.get(currentQuestion).answer){
            score += 10
//            Toast.makeText(this, "score $score",Toast.LENGTH_SHORT).show()
        }
        currentQuestion++
        if(currentQuestion >= questionList.size){
            if(score >= (score/questionList.size*10)*100){
                binding.win.visibility = View.VISIBLE
                binding.option1.visibility = View.INVISIBLE
                binding.option2.visibility = View.INVISIBLE
                binding.option3.visibility = View.INVISIBLE
                binding.option4.visibility = View.INVISIBLE
                Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance+1)
                var isUpdated = false

            }else{
                binding.losing.visibility = View.VISIBLE
                binding.option1.visibility = View.INVISIBLE
                binding.option2.visibility = View.INVISIBLE
                binding.option3.visibility = View.INVISIBLE
                binding.option4.visibility = View.INVISIBLE
            }
        }
        else {
            binding.question.text = questionList.get(currentQuestion).question
            binding.option1.text = questionList.get(currentQuestion).option1
            binding.option2.text = questionList.get(currentQuestion).option2
            binding.option3.text = questionList.get(currentQuestion).option3
            binding.option4.text = questionList.get(currentQuestion).option4
        }
    }
}