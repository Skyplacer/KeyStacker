package com.example.keystacker

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.example.keystacker.UserContract.Users
class UserRepository(private val dbHelper: UserDBHelper) {

    fun createUser(email: String, displayName: String, passwordHash: String): Long {
        val db = dbHelper.writableDatabase
        val normalizedEmail = email.trim().lowercase()
        val values = ContentValues().apply {
            put(Users.COL_EMAIL, normalizedEmail)
            put(Users.COL_DISPLAY_NAME, displayName.trim())
            put(Users.COL_PASSWORD_HASH, passwordHash)
        }
        val rowId = db.insertWithOnConflict(
            UserContract.Users.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
        return try {
            // If email already exists (UNIQUE), this returns -1 instead of throwing
            db.insertWithOnConflict(
                Users.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
            )
        } catch (e: SQLiteConstraintException) {
            -1L
        } finally {
            db.close()
            return rowId
        }
    }

    fun verifyByEmail(email: String, plainPassword: String): Boolean {
        val db = dbHelper.readableDatabase
        val normalizedEmail = email.trim().lowercase()

        val cursor = db.query(
            UserContract.Users.TABLE_NAME,
            arrayOf(UserContract.Users.COL_PASSWORD_HASH),
            "${UserContract.Users.COL_EMAIL} = ?",
            arrayOf(normalizedEmail),
            null, null, null,
            "1"
        )
        var ok = false
        cursor.use {
            if (it.moveToFirst()) {
                val stored = it.getString(0)
                ok = stored == Hashing.sha256(plainPassword)
            }
        }
        db.close()
        return ok
    }

    data class User(
        val id: Long,
        val email: String,
        val displayName: String,
        val passwordHash: String,
        val createdAt: Long
    )

    fun getUserByEmail(email: String): User? {
        val db = dbHelper.readableDatabase
        val c = db.query(
            UserContract.Users.TABLE_NAME,
            arrayOf(
                UserContract.Users.COL_ID,
                UserContract.Users.COL_EMAIL,
                UserContract.Users.COL_DISPLAY_NAME,
                UserContract.Users.COL_PASSWORD_HASH,
                UserContract.Users.COL_CREATED_AT
            ),
            "${UserContract.Users.COL_EMAIL}=?",
            arrayOf(email.trim().lowercase()),
            null, null, null,
            "1"
        )
        var user: User? = null
        c.use {
            if (it.moveToFirst()) {
                user = User(
                    id = it.getLong(0),
                    email = it.getString(1),
                    displayName = it.getString(2),
                    passwordHash = it.getString(3),
                    createdAt = it.getLong(4)
                )
            }
        }
        db.close()
        return user
    }
    fun changePasswordByEmail(email: String, currentPlain: String, newPlain: String): Boolean {
        val db = dbHelper.writableDatabase
        val normalizedEmail = email.trim().lowercase()
        val currentHash = Hashing.sha256(currentPlain)
        val newHash = Hashing.sha256(newPlain)

        // Atomic: only update if email + current hash match
        val sql = """
        UPDATE ${UserContract.Users.TABLE_NAME}
        SET ${UserContract.Users.COL_PASSWORD_HASH} = ?
        WHERE ${UserContract.Users.COL_EMAIL} = ?
          AND ${UserContract.Users.COL_PASSWORD_HASH} = ?
        """.trimIndent()

        val stmt = db.compileStatement(sql)
        stmt.bindString(1, newHash)
        stmt.bindString(2, normalizedEmail)
        stmt.bindString(3, currentHash)

        val rows = try {
            stmt.executeUpdateDelete()
        } finally {
            db.close()
        }
        return rows > 0
    }

    fun deleteByEmail(email: String): Boolean {
        val db = dbHelper.writableDatabase
        val rows = try {
            db.delete(
                UserContract.Users.TABLE_NAME,
                "${UserContract.Users.COL_EMAIL} = ?",
                arrayOf(email.trim().lowercase())
            )
        } finally {
            db.close()
        }
        return rows > 0
    }

    fun deleteByEmailWithPassword(email: String, currentPlain: String): Boolean {
        val ok = verifyByEmail(email, currentPlain)
        return if (ok) deleteByEmail(email) else false
    }

    fun emailExists(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val c = db.rawQuery(
            "SELECT 1 FROM ${UserContract.Users.TABLE_NAME} WHERE ${UserContract.Users.COL_EMAIL}=? LIMIT 1",
            arrayOf(email.trim().lowercase())
        )
        val exists = c.use { it.moveToFirst() }
        db.close()
        return exists
    }

    fun setPasswordByEmail(email: String, newPlain: String): Boolean {
        val db = dbHelper.writableDatabase
        val rows = try {
            val newHash = Hashing.sha256(newPlain)
            db.execSQL(
                "UPDATE ${UserContract.Users.TABLE_NAME} SET ${UserContract.Users.COL_PASSWORD_HASH}=? WHERE ${UserContract.Users.COL_EMAIL}=?",
                arrayOf(newHash, email.trim().lowercase())
            )
            val check = db.rawQuery(
                "SELECT 1 FROM ${UserContract.Users.TABLE_NAME} WHERE ${UserContract.Users.COL_EMAIL}=? AND ${UserContract.Users.COL_PASSWORD_HASH}=? LIMIT 1",
                arrayOf(email.trim().lowercase(), newHash)
            )
            check.use { it.moveToFirst() }
        } finally {
            db.close()
        }
        return rows
    }
}