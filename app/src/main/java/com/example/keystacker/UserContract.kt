package com.example.keystacker

import android.provider.BaseColumns

object UserContract {
    object Users : BaseColumns {
        const val TABLE_NAME = "users"
        const val COL_ID = BaseColumns._ID
        const val COL_EMAIL = "email"
        const val COL_DISPLAY_NAME = "display_name"
        const val COL_PASSWORD_HASH = "password_hash"
        const val COL_CREATED_AT = "created_at"
    }
}