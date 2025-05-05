package com.example.createplanetapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.test.ViewPagerAdapter

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val photos: ViewPager2 = findViewById(R.id.item_images)
        photos.adapter = ViewPagerAdapter(intent.getStringArrayExtra("itemPhotos")?.toList() ?: emptyList())

        val items = csvParser(resources, R.raw.po_gorodu)
        items.addAll(csvParser(resources, R.raw.zagorodnie))

        val title: TextView = findViewById(R.id.item_title)
        val desc: TextView = findViewById(R.id.item_short_description)
        title.text = intent.getStringExtra("itemTitle")
        desc.text = intent.getStringExtra("itemDesc")

        val item = items[intent.getIntExtra("position", 0)]
        val excursions = item.excursions


        val spinner_1: Spinner = findViewById(R.id.spinner_composition)
        val spinner_2: Spinner = findViewById(R.id.spinner_duration)
        val spinner_3: Spinner = findViewById(R.id.spinner_class)

        val adapter_1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, excursions.keys.toList())
        adapter_1.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner_1.adapter = adapter_1

        val adapter_2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, excursions.flatMap { it.value.keys })
        adapter_2.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner_2.adapter = adapter_2

        val adapter_3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, excursions.flatMap { it.value.values.flatMap { it.keys } })
        adapter_3.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner_3.adapter = adapter_3

    }
}