package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Favorites : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val itemsList: RecyclerView = view.findViewById(R.id.favorites_list)
        val items = csvParser(resources, R.raw.po_gorodu)
        items.addAll(csvParser(resources, R.raw.zagorodnie))

        val dbHelper = DBHelper(requireContext(), null)
        val favoriteItems = dbHelper.getRecords(items, true, false)

        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.adapter = FavoritesAdapter(favoriteItems, requireContext())

        val emptyText: TextView = view.findViewById(R.id.empty_favorites_txt)
        if (favoriteItems.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            itemsList.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            itemsList.visibility = View.VISIBLE
        }

        return view
    }
}
