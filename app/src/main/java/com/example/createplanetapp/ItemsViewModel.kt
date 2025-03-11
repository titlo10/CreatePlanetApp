package com.example.createplanetapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemsViewModel(val images: IntArray, val title: String, val description: String) : Parcelable