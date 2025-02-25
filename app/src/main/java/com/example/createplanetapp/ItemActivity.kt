package com.example.createplanetapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val title : TextView = findViewById(R.id.item_title)
        val viewPager : ViewPager2 = findViewById(R.id.view_pager)
        val wormDotsIndicator : WormDotsIndicator = findViewById(R.id.worm_dots_indicator)

        val imageList = intent.getIntArrayExtra("imageList")
        if(imageList != null){
            val customAdapter = ViewPagerAdapter(imageList)
            viewPager.adapter = customAdapter
        }
        wormDotsIndicator.attachTo(viewPager)
        title.text = intent.getStringExtra("itemTitle")

    }
}