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
        const val TABLE_NAME = "FAVORITE_AND_ORDERED"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val FAVORITE_STATUS = "fStatus"
        const val ORDERED_STATUS = "oStatus"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query  = """
            CREATE TABLE $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME_COL TEXT UNIQUE,
                $FAVORITE_STATUS BOOLEAN,
                $ORDERED_STATUS BOOLEAN
            )
        """.trimIndent()
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /** Function updates record if that exists else inserts new */
    fun upsertRecord(name: String, fStatus: Boolean, oStatus: Boolean) {
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(FAVORITE_STATUS, fStatus)
            put(ORDERED_STATUS, oStatus)
        }

        writableDatabase.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    /**
     * Function searches for favorite or ordered matching depends on what you specify
     * @param fStatus
     * @param oStatus
     * Use them to specify the field or fields need to search
     */
    fun getRecords(items: List<ItemsViewModel>, fStatus: Boolean, oStatus: Boolean) : ArrayList<ItemsViewModel> {
        val db = this.readableDatabase

        val favoriteItems = ArrayList<ItemsViewModel>()

        val cursorTable : Cursor = db.rawQuery("""
            SELECT * FROM $TABLE_NAME
            WHERE $FAVORITE_STATUS = $fStatus
            OR $ORDERED_STATUS = $oStatus
        """.trimIndent(), null)

        cursorTable.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    items.forEach {
                        if(it.title == cursor.getString(1)) {
                            favoriteItems.add(it)
                        }
                    }
                } while(cursor.moveToNext())
            }
        }

        return favoriteItems
    }

}