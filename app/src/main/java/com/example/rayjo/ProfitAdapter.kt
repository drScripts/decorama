package com.example.rayjo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayjo.model.ProfitModelData

class ProfitAdapter(private val listProfit : ArrayList<ProfitModelData>) : RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder>() {
    class ProfitViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        val date : TextView = item.findViewById(R.id.date_profit)
        val profit : TextView = item.findViewById(R.id.tv_final_profit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfitViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.list_profit,parent,false)
        return ProfitViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ProfitViewHolder, position: Int) {
        holder.date.text = listProfit[position].date
        holder.profit.text = listProfit[position].profit
    }

    override fun getItemCount(): Int = listProfit.size
}








