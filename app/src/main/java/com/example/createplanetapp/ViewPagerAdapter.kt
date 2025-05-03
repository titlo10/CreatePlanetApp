package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.createplanetapp.R

class ViewPagerAdapter(private val imageList : List<String?>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

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
        holder.pagerImage.load(imageId)
    }
}
