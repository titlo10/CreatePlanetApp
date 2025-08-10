package com.example.createplanetapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GOODS"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "SIMPLE_TABLE"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val FAVORITE_STATUS = "favoriteStatus"
        const val ORDERED_STATUS = "orderedStatus"
        const val PAID_STATUS = "paidStatus"

        val STATUS_COLUMNS = listOf(FAVORITE_STATUS, ORDERED_STATUS, PAID_STATUS)

        const val STATUS_TRUE = "true"
        const val STATUS_FALSE = "false"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query  = """
            CREATE TABLE $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME_COL TEXT UNIQUE,
                $FAVORITE_STATUS TEXT,
                $ORDERED_STATUS TEXT,
                $PAID_STATUS TEXT
            )
        """.trimIndent()
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun setStatus(name: String, column: String, status: String) {
        val db = writableDatabase
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_NAME WHERE $NAME_COL = ?",
            arrayOf(name)
        )

        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(column, status)
        }

        cursor.use {
            if (it.moveToFirst()) {
                db.update(TABLE_NAME, values, "$NAME_COL = ?", arrayOf(name))
            } else {
                STATUS_COLUMNS.filter { it != column }.forEach { otherCol ->
                    values.put(otherCol, STATUS_FALSE)
                }
                db.insert(TABLE_NAME, null, values)
            }
        }
    }

    /** Functions below set defined status for the record and creates it, if it doesn't exist */

    fun setFavoriteStatus(name: String, status: String) = setStatus(name, FAVORITE_STATUS, status)
    fun setOrderedStatus(name: String, status: String) = setStatus(name, ORDERED_STATUS, status)
    fun setPaidStatus(name: String, status: String) = setStatus(name, PAID_STATUS, status)

    private fun getRecords(items: List<ItemsViewModel>, column: String): ArrayList<ItemsViewModel> {
        val db = readableDatabase
        val result = ArrayList<ItemsViewModel>()

        val cursorTable = db.rawQuery(
            "SELECT $NAME_COL FROM $TABLE_NAME WHERE $column = ?",
            arrayOf(STATUS_TRUE)
        )

        cursorTable.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    items.forEach {
                        if(it.title == cursor.getString(0)) {
                            result.add(it)
                        }
                    }
                } while(cursor.moveToNext())
            }
        }

        return result
    }

    /** Functions below searches for the defined status */

    fun getFavoriteRecords(items: List<ItemsViewModel>) = getRecords(items, FAVORITE_STATUS)
    fun getOrderedRecords(items: List<ItemsViewModel>) = getRecords(items, ORDERED_STATUS)
    fun getPaidRecords(items: List<ItemsViewModel>) = getRecords(items, PAID_STATUS)

    private fun hasStatus(name: String, column: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $column FROM $TABLE_NAME WHERE $NAME_COL = ?",
            arrayOf(name)
        )

        return cursor.use {
            if (cursor.moveToFirst()) cursor.getString(0) == STATUS_TRUE else false
        }
    }

    /** Functions below return the status value of specified name */

    fun isFavorite(name: String) = hasStatus(name, FAVORITE_STATUS)
    fun isOrdered(name: String) = hasStatus(name, ORDERED_STATUS)
    fun isPaid(name: String) = hasStatus(name, PAID_STATUS)
}