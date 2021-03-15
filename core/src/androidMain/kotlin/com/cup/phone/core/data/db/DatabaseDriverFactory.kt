package com.cup.phone.core.data.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MessagesDb.Schema, context, "messages.db")
    }

    actual fun createMessagesDb(): MessagesDb {
      return MessagesDb(createDriver())
    }
}