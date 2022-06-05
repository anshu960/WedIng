package com.example.weding.activity.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
   SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "WedingDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_WED = "WedingTable"

        private const val KEY_ID = "_id"
        private const val KEY_COUPLE = "couple1"
        private const val KEY_COUPLE2 = "couple2"
        private const val KEY_ADDRESS = "address"
        private const val KEY_DATE = "date"
        private const val KEY_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val WEDDIN_TABLE = ("CREATE TABLE " + TABLE_WED + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_COUPLE + " TEXT,"
                + KEY_COUPLE2 + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_IMAGE + " TEXT)")
        db?.execSQL(WEDDIN_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_WED")
        onCreate(db)
    }

    fun addWedding(wedding: SqliteDatabase): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ADDRESS, wedding.address)
        contentValues.put(KEY_COUPLE, wedding.coupleName)
        contentValues.put(KEY_COUPLE2, wedding.coupleName2)
        contentValues.put(KEY_DATE, wedding.date)
        contentValues.put(KEY_IMAGE, wedding.image)

        val result = db.insert(TABLE_WED, null, contentValues)
        db.close()
        return result

    }


    @SuppressLint("Range")
    fun eventList(): ArrayList<SqliteDatabase> {
        val eventList = ArrayList<SqliteDatabase>()
        val selectQuery = "SELECT * FROM $TABLE_WED"
        val db = this.readableDatabase
        val cursor: Cursor? = null

        try {
          val cursor: Cursor =  db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()){
                do {
                    val place = SqliteDatabase(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_COUPLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_COUPLE2)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE))
                            )
                    eventList.add(place)
                }while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return eventList
    }

}



