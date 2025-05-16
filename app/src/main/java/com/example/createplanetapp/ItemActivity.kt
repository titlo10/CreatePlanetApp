package com.example.createplanetapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.test.ViewPagerAdapter

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val items = csvParser(resources, R.raw.po_gorodu)
        items.addAll(csvParser(resources, R.raw.zagorodnie))

        val photos: ViewPager2 = findViewById(R.id.item_images)
        val title: TextView = findViewById(R.id.item_title)
        val desc: TextView = findViewById(R.id.item_short_description)
        val back_arrow: ImageView = findViewById(R.id.back_arrow)
        val spinner1: Spinner = findViewById(R.id.spinner_composition)
        val spinner2: Spinner = findViewById(R.id.spinner_duration)
        val spinner3: Spinner = findViewById(R.id.spinner_class)

        title.text = intent.getStringExtra("itemTitle")
        desc.text = intent.getStringExtra("itemDesc")

        photos.adapter = ViewPagerAdapter(intent.getStringArrayExtra("itemPhotos")?.toList() ?: emptyList())

        val item = items[intent.getIntExtra("position", 0)]
        val excursions = item.excursions

        val setComposition : MutableSet<String> = mutableSetOf()
        val setDuration : MutableSet<String> = mutableSetOf()
        val setClass : MutableSet<String> = mutableSetOf()

        setComposition.addAll(excursions.keys.toList())
        setDuration.addAll(excursions.flatMap { it.value.keys })
        setClass.addAll(excursions.flatMap { it.value.values.flatMap { it.keys } })

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, setComposition.toList())
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner1.adapter = adapter1

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, setDuration.toList())
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner2.adapter = adapter2

        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, setClass.toList())
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner3.adapter = adapter3

        back_arrow.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, Orders()).commit()
        }
    }
}