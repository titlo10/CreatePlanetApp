package com.example.createplanetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import android.content.Intent


class OrdersAdapter(var items: List<ItemsViewModel>, val context: android.content.Context): RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.item_in_list_image)
        val title: TextView = view.findViewById(R.id.item_in_list_title)
        val desc: TextView = view.findViewById(R.id.item_in_list_description)
        val button: Button = view.findViewById(R.id.item_in_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list_orders, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.desc.text = items[position].description
        holder.image.load(items[position].photo[0])

        holder.button.setOnClickListener{
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemTitle", items[position].title)
            intent.putExtra("itemDesc", items[position].description)
            intent.putExtra("itemText", items[position].text)
            intent.putExtra("itemPhotos", items[position].photo.toTypedArray())
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }
}
