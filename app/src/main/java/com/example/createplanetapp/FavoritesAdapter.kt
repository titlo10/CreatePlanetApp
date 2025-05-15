package com.example.createplanetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.createplanetapp.FavoritesAdapter.MyViewHolder

class FavoritesAdapter(var items: List<ItemsViewModel>, val context: android.content.Context): RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>(){

    val dbHelper = DBHelper(context, null)

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_in_list_favorites_image)
        val title: TextView = view.findViewById(R.id.item_in_list_favorites_title)
        val desc: TextView = view.findViewById(R.id.item_in_list_favorites_description)
        val button: Button = view.findViewById(R.id.item_in_list_favorites_button)
        val heart: ImageView = view.findViewById(R.id.item_in_list_favorites_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list_favorites, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.MyViewHolder, position: Int) {
       val item = items[position]
        holder.title.text = items[position].title
        holder.desc.text = items[position].description
        holder.image.load(items[position].photo[0])

        updateHeartIcon(holder.heart, item.title)

        holder.button.setOnClickListener{
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemTitle", items[position].title)
            intent.putExtra("itemDesc", items[position].description)
            intent.putExtra("itemText", items[position].text)
            intent.putExtra("itemPhotos", items[position].photo.toTypedArray())
            intent.putExtra("position", position)
            context.startActivity(intent)
        }

        holder.heart.setOnClickListener {
            val currentFavorites = dbHelper.getRecords(items, true, false)
            val isFavorite = currentFavorites.any { it.title == item.title }

            dbHelper.upsertRecord(item.title, !isFavorite, false)

            updateHeartIcon(holder.heart, item.title)

            if (!isFavorite) {
                val newItems = items.toMutableList()
                newItems.removeAt(position)
                items = newItems
                notifyItemRemoved(position)
            }
        }
    }

    fun updateHeartIcon(heartIcon: ImageView, itemTitle: String){
        val currentFavorites = dbHelper.getRecords(items, true, false)
        val isFavorite = currentFavorites.any { it.title == itemTitle }
        heartIcon.setImageResource(
            if (isFavorite) R.drawable.heart_full_blue
            else R.drawable.favorite_menu_icon
        )
    }


}