package com.example.createplanetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private val imageList : IntArray) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pagerImage : ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()  = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageId = imageList[position]
        holder.pagerImage.setImageResource(imageId)
    }
}