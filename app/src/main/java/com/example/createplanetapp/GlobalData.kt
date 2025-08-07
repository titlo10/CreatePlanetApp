package com.example.createplanetapp

import android.content.Context

/**
 * This class contains all parsed items from R.raw.* in `ArrayList<ItemViewModel>` data type
 *
 * `GlobalData.initialize` - initial parsing
 *
 * `GlobalData.items` - returns all items
 */
object GlobalData {
    private var _items: ArrayList<ItemsViewModel>? = null  // Item — ваша модель данных

    val items: ArrayList<ItemsViewModel>
        get() = _items ?: throw IllegalStateException("Данные не загружены. Вызовите GlobalData.initialize()")

    fun initialize(context: Context) {
        if (_items == null) {
            _items = parseCsv(context)
        }
    }
    private fun parseCsv(context: Context): ArrayList<ItemsViewModel> {
        val items = ArrayList<ItemsViewModel>()
        items += csvParser(context.resources, R.raw.po_gorodu)
        items += csvParser(context.resources, R.raw.zagorodnie)
        return items
    }
}