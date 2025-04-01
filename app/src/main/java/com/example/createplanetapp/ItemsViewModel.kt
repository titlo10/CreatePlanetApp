package com.example.createplanetapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemsViewModel(val brand: String,
                 val mark : String,
                 val category : List<String>,
                 val title: String,
                 val description: String,
                 val text: String,
                 val photo: List<String>,
                 val excursions : MutableMap<String, MutableMap<String, MutableMap<String, Int>>> = mutableMapOf()
) : Parcelable