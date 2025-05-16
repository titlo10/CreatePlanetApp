package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Orders : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        val itemsList: RecyclerView = view.findViewById(R.id.orders_list)
        val items = csvParser(resources, R.raw.po_gorodu)
        items.addAll(csvParser(resources, R.raw.zagorodnie))

        val dbHelper = DBHelper(requireContext(), null)
        val orderedItems = dbHelper.getRecords(items, false, true)

        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.adapter = OrdersAdapter(orderedItems, requireContext())

        val emptyText: TextView = view.findViewById(R.id.empty_orders_txt)
        if (orderedItems.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            itemsList.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            itemsList.visibility = View.VISIBLE
        }

        return view
    }
}
