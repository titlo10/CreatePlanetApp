package com.example.createplanetapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Spinner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)

        val spinner: Spinner = findViewById(R.id.action_bar_spinner)

        val listItems = listOf("Комфорт", "Бизнес", "VIP")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        
    }
}