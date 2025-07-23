package com.example.createplanetapp

import android.content.Context

/**
 * USAGE:
 *
 *   val `<name>` = GlobalData.items`[<index>]`
 *
 *   `<name>` - your own name for variable which would be in use
 *
 *   `<index>` should be chose if needed:
- default: list of all excursions, no boundaries by type
- 0: po_gorodu.csv
- 1: zagorodnie.csv
 */
object GlobalData {
    private var _items: List<ArrayList<ItemsViewModel>>? = null  // Item — ваша модель данных

    val items: List<ArrayList<ItemsViewModel>>
        get() = _items ?: throw IllegalStateException("Данные не загружены. Вызовите GlobalData.initialize()")

    fun initialize(context: Context) {
        if (_items == null) {
            _items = parseCsv(context)
        }
    }
    private fun parseCsv(context: Context): List<ArrayList<ItemsViewModel>> {
        val items = mutableListOf<ArrayList<ItemsViewModel>>()
        items.add(csvParser(context.resources, R.raw.po_gorodu))
        items.add(csvParser(context.resources, R.raw.zagorodnie))
        return items
    }
}