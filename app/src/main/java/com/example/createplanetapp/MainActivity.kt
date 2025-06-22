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

        loadFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> loadFragment(Home())
                R.id.bottom_catalog -> loadFragment(Catalog())
                R.id.bottom_favorites -> loadFragment(Favorites())
                R.id.bottom_orders -> loadFragment(Orders())
                else -> {}
            }
            true
        }
    }

    fun loadFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
}
