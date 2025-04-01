package com.example.createplanetapp

import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.InputStreamReader
import org.apache.commons.csv.CSVFormat

fun csvParser(resources: Resources, @RawRes rawResId: Int) : ArrayList<ItemsViewModel>
{
    resources.openRawResource(rawResId).use { inputStream ->
        val reader = InputStreamReader(inputStream)
        val goods = arrayListOf<ItemsViewModel>()

        val records = CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setDelimiter(';')
            .setHeader()
            .setSkipHeaderRecord(true)
            .build()
            .parse(reader)

        for (record in records) {
            if (record.get("SKU").isNullOrBlank()) {
                goods.add(ItemsViewModel(
                    record.get("Brand"),
                    record.get("Mark"),
                    record.get("Category").split(";"),
                    record.get("Title"),
                    record.get("Description"),
                    record.get("Text"),
                    record.get("Photo").split(" ")
                ))
            } else {
                val line = record.get("Title").split(" - ")
                goods.last().excursions
                    .getOrPut(line[1]){ mutableMapOf() }
                    .getOrPut(line[2]){ mutableMapOf() }
                    .getOrPut(line[3]){ record.get("Price").toDouble().toInt() }
            }
        }
        return goods
    }
}