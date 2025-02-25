package com.example.createplanetapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val itemList: List<ItemsViewModel>, private val context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.image_list)
        val textView : TextView = itemView.findViewById(R.id.text_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = itemList[position]
        holder.imageView.setImageResource(itemsViewModel.images[0])
        holder.textView.text = itemsViewModel.title
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemTitle", itemsViewModel.title)
            intent.putExtra("imageList", itemsViewModel.images)
            context.startActivity(intent)
        }
    }
}