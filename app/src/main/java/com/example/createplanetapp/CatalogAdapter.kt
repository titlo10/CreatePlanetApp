package com.example.createplanetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class CatalogAdapter(var items: List<ItemsViewModel>, val context: android.content.Context): RecyclerView.Adapter<CatalogAdapter.CatalogItemHolder>() {

    private val dbHelper = DBHelper(context, null)

    class CatalogItemHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageButton: ImageButton = view.findViewById(R.id.item_in_list_catalog_imageButton)
        val title: TextView = view.findViewById(R.id.item_in_list_catalog_title)
        val desc: TextView = view.findViewById(R.id.item_in_list_catalog_description)
        val price: TextView = view.findViewById(R.id.item_in_list_catalog_price)
        val heart: ImageView = view.findViewById(R.id.item_in_list_catalogStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list_catalog, parent, false)
        return CatalogItemHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CatalogItemHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = item.description
        holder.imageButton.load(item.photo[0])
        holder.price.text = "от " + item.excursions.values.first().values.first().values.first().toString() + " р."

        //Переход на страничку с товаром
        holder.imageButton.setOnClickListener{
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemTitle", item.title)
            intent.putExtra("itemDesc", item.description)
            intent.putExtra("itemText", item.text)
            intent.putExtra("itemPhotos", item.photo.toTypedArray())
            intent.putExtra("position", position)
            context.startActivity(intent)
        }

        //Иконка избранного
        updateFavoriteIcon(holder.heart, item.title)
        holder.heart.setOnClickListener {
            val currentFavorites = dbHelper.getRecords(items, true, false)
            val isFavorite = currentFavorites.any { it.title == item.title }
            dbHelper.upsertRecord(item.title, !isFavorite, false)
            updateFavoriteIcon(holder.heart, item.title)
        }

    }

    private fun updateFavoriteIcon(icon: ImageView, title: String) {
        val isFavorite = dbHelper.getRecords(items, true, false).any { it.title == title }
        icon.setImageResource(if (isFavorite) R.drawable.heart_full_blue else R.drawable.favorite_menu_icon)
    }
}