package com.example.createplanetapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

class Catalog : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        val data = arguments?.getParcelableArrayList<ItemsViewModel>("dataTest") ?: arrayListOf()

        //val spinner: Spinner = view.findViewById(R.id.catalog_options_spinner)
        //var tourOptions = arrayOf("Школьникам", "Семьям")
        //var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, tourOptions)


        return view
    }
}