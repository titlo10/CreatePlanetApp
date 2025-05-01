package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.adapter = OrdersAdapter(items, this)

        return view
    }
}
