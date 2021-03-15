package com.cup.phone.core.data.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MessagesDb.Schema, "messages.db")
    }

    actual fun createMessagesDb(): MessagesDb {
       return MessagesDb(createDriver())
    }
}
