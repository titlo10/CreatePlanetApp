package com.example.createplanetapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.createplanetapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val items = arrayListOf<ItemsViewModel>()

        val firstList = intArrayOf(R.drawable._1_1, R.drawable._1_2, R.drawable._1_3, R.drawable._1_4, R.drawable._1_5, R.drawable._1_6)
        val secondList = intArrayOf(R.drawable._2_1, R.drawable._2_2, R.drawable._2_3, R.drawable._2_4, R.drawable._2_5, R.drawable._2_6)
        val thirdList = intArrayOf(R.drawable._3_1, R.drawable._3_2, R.drawable._3_3, R.drawable._3_4, R.drawable._3_5, R.drawable._3_6)
        val fourthList = intArrayOf(R.drawable._4_1, R.drawable._4_2, R.drawable._4_3, R.drawable._4_4, R.drawable._4_5, R.drawable._4_6)

        items.add(ItemsViewModel(firstList, "Знакомство с городом", "Классическая обзорная экскурсия по центру"))
        items.add(ItemsViewModel(secondList, "Петергоф. Триумф Петра", "Резиденция великого императора"))
        items.add(ItemsViewModel(thirdList, "В сердце старого города", "История зарождения Санкт-Петербурга"))
        items.add(ItemsViewModel(fourthList, "Парадные залы Эрмитажа", "Классическая экскурсия по главным залам музея"))

        loadFragment(Home(), items)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> loadFragment(Home(), items)
                R.id.bottom_catalog -> loadFragment(Catalog(), items)
                R.id.bottom_favorites -> loadFragment(Favorites(), items)
                R.id.bottom_orders -> loadFragment(Orders(), items)
                else -> {}
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, data: ArrayList<ItemsViewModel>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("dataTest", data)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }
}