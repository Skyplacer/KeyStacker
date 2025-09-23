package com.example.keystacker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.keystacker.UserContract.Users

class UserDBHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val create = """
            CREATE TABLE ${Users.TABLE_NAME} (
              ${Users.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
              ${Users.COL_EMAIL} TEXT NOT NULL UNIQUE,
              ${Users.COL_DISPLAY_NAME} TEXT NOT NULL,
              ${Users.COL_PASSWORD_HASH} TEXT NOT NULL,
              ${Users.COL_CREATED_AT} INTEGER NOT NULL DEFAULT (strftime('%s','now'))
            )
        """.trimIndent()
        db.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Users.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "app.db"
        private const val DB_VERSION = 1
    }
}