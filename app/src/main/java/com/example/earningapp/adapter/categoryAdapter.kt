package com.example.earningapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.QuizActivity
import com.example.earningapp.databinding.CategoryItemBinding
import com.example.earningapp.model.categoryModelClass

class categoryAdapter(
    var categoryList: ArrayList<categoryModelClass>,
    var requireActivity: FragmentActivity
) : RecyclerView.Adapter<categoryAdapter.MyCategoryViewHolder>() {
    class MyCategoryViewHolder(var binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = categoryList.size

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        var dataList = categoryList[position]
        holder.binding.categoryimage.setImageResource(dataList.catImage)
        holder.binding.category.text = dataList.catText
        holder.binding.categorybtn.setOnClickListener {
            var intent = Intent(requireActivity, QuizActivity::class.java)
            intent.putExtra("categoryimg", dataList.catImage)
            intent.putExtra("questionType", dataList.catText)
            requireActivity.startActivity(intent)
        }
    }

}