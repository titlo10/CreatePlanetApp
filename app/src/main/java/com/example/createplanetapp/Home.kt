package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        val items = arrayListOf<ItemsViewModel>()

        val firstList = intArrayOf(R.drawable._1_1, R.drawable._1_2, R.drawable._1_3, R.drawable._1_4, R.drawable._1_5, R.drawable._1_6)
        val secondList = intArrayOf(R.drawable._2_1, R.drawable._2_2, R.drawable._2_3, R.drawable._2_4, R.drawable._2_5, R.drawable._2_6)
        val thirdList = intArrayOf(R.drawable._3_1, R.drawable._3_2, R.drawable._3_3, R.drawable._3_4, R.drawable._3_5, R.drawable._3_6)
        val fourthList = intArrayOf(R.drawable._4_1, R.drawable._4_2, R.drawable._4_3, R.drawable._4_4, R.drawable._4_5, R.drawable._4_6)

        items.add(ItemsViewModel(firstList, "Знакомство с городом"))
        items.add(ItemsViewModel(secondList, "Петергоф. Триумф Петра"))
        items.add(ItemsViewModel(thirdList, "В сердце старого города"))
        items.add(ItemsViewModel(fourthList, "Парадные залы Эрмитажа"))

        val customAdapter = CustomAdapter(items, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = customAdapter

        return view
    }
}