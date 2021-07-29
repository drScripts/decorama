package com.example.rayjo

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rayjo.model.DetailDataPlaceHolder

class HistoryItemAdapter(private val historyListItem : ArrayList<DetailDataPlaceHolder>,private val context : Context) : RecyclerView.Adapter<HistoryItemAdapter.HistoryItemViewHolder>() {
    class HistoryItemViewHolder(item : View) : RecyclerView.ViewHolder(item)  {
        val date : TextView = item.findViewById(R.id.date_show)
        val img : ImageView = item.findViewById(R.id.logo_brand)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.each_item,parent,false)
        return  HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        if (historyListItem[position].type == "youtube"){
            Glide.with(holder.itemView.context)
                .load(R.drawable.youtube_logo_hd)
                .into(holder.img)
        }else{
            Glide.with(holder.itemView.context)
                .load(R.drawable.netflix_logo_hd)
                .into(holder.img)
        }
            holder.date.text = historyListItem[position].date

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context,DetailTransaction::class.java)
            intent.putExtra(DetailTransaction.EXTRA_DETAIL_DATA,historyListItem[position])
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = historyListItem.size
}