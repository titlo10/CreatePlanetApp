package com.example.createplanetapp

import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.TextView
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

        val excursions = intent.getStringExtra("itemExcursions")
        val text = intent.getStringExtra("itemText")

        val expandableList: ExpandableListView = findViewById(R.id.expandablelistview)
        expandableList.setIndicatorBounds(0,150)
        val groups = listOf("Об экскурсии")
        val item = mapOf(
            "Об экскурсии" to listOf(text?.replace("<br />", ""))
            )

        // Создание адаптера
        val adapter = ExpandableListAdapter(this, groups, item)
        expandableList.setAdapter(adapter)
    }
}