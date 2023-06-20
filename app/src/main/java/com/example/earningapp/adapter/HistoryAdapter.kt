package com.example.earningapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.databinding.HistoryItemBinding
import com.example.earningapp.model.HistoryModelClass
import java.sql.Timestamp
import java.util.Date

class HistoryAdapter(var listHistory: ArrayList<HistoryModelClass>): RecyclerView.Adapter<HistoryAdapter.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
        return HistoryCoinViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {
        var timeStamp = Timestamp(listHistory[position].timeAndDate.toLong())
        holder.binding.dateAndTime.text = Date(timeStamp.time).toString()
        holder.binding.status.text = if(listHistory[position].isWithdrawl){"- Money Withdrawl"} else{"+ Money Credited"}
        holder.binding.coinChange.text = listHistory[position].coin
    }
}