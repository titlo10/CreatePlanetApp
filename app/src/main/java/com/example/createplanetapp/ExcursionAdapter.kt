package com.example.createplanetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ExcursionAdapter(
    private val items: List<ItemsViewModel>,
    private val context: android.content.Context
) : RecyclerView.Adapter<ExcursionAdapter.MyViewHolder>() {

    private val dbHelper = DBHelper(context, null)

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_in_list_image)
        val title: TextView = view.findViewById(R.id.item_in_list_title)
        val desc: TextView = view.findViewById(R.id.item_in_list_description)
        val favoriteIcon: ImageView = view.findViewById(R.id.favorite_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_list_home, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.desc.text = item.description

        if (item.photo.isNotEmpty()) {
            holder.image.load(item.photo[0]) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error)
            }
        }

        // Обновляем иконку избранного
        updateFavoriteIcon(holder.favoriteIcon, item.title)

        holder.favoriteIcon.setOnClickListener {
            val currentFavorites = dbHelper.getRecords(items, true, false)
            val isFavorite = currentFavorites.any { it.title == item.title }
            dbHelper.upsertRecord(item.title, !isFavorite, false)
            updateFavoriteIcon(holder.favoriteIcon, item.title)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java).apply {
                putExtra("itemTitle", item.title)
                putExtra("itemDesc", item.description)
                putExtra("itemText", item.text)
                putExtra("itemPhotos", item.photo.toTypedArray())
                putExtra("position", position)
            }
            context.startActivity(intent)
        }
    }

    private fun updateFavoriteIcon(icon: ImageView, title: String) {
        val isFavorite = dbHelper.getRecords(items, true, false).any { it.title == title }
        icon.setImageResource(if (isFavorite) R.drawable.heart_full_blue else R.drawable.favorite_menu_icon)
    }
}