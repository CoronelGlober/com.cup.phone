package com.cup.phone.core.data.db.core

import com.cup.phone.core.data.db.Messages
import com.cup.phone.core.data.db.MessagesDb
import com.cup.phone.core.data.db.MessagesQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.reflect.KClass

internal val KClass<MessagesDb>.schema: SqlDriver.Schema
  get() = MessagesDbImpl.Schema

internal fun KClass<MessagesDb>.newInstance(driver: SqlDriver): MessagesDb = MessagesDbImpl(driver)

private class MessagesDbImpl(
  driver: SqlDriver
) : TransacterImpl(driver), MessagesDb {
  override val messagesQueries: MessagesQueriesImpl = MessagesQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE messages (
          |  key INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |  timestamp INTEGER NOT NULL,
          |  userName TEXT NOT NULL,
          |  userColor TEXT NOT NULL,
          |  message TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class MessagesQueriesImpl(
  private val database: MessagesDbImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), MessagesQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAll(mapper: (
    key: Long,
    timestamp: Long,
    userName: String,
    userColor: String,
    message: String
  ) -> T): Query<T> = Query(-454878879, selectAll, driver, "Messages.sq", "selectAll",
      "SELECT * FROM messages ORDER BY timestamp DESC") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!
    )
  }

  override fun selectAll(): Query<Messages> = selectAll { key, timestamp, userName, userColor,
      message ->
    Messages(
      key,
      timestamp,
      userName,
      userColor,
      message
    )
  }

  override fun insertMessage(
    timestamp: Long,
    userName: String,
    userColor: String,
    message: String
  ) {
    driver.execute(1188737898,
        """INSERT INTO messages(timestamp, userName, userColor, message) VALUES (?, ?, ?, ?)""", 4)
        {
      bindLong(1, timestamp)
      bindString(2, userName)
      bindString(3, userColor)
      bindString(4, message)
    }
    notifyQueries(1188737898, {database.messagesQueries.selectAll})
  }

  override fun deleteAll() {
    driver.execute(902387282, """DELETE FROM messages""", 0)
    notifyQueries(902387282, {database.messagesQueries.selectAll})
  }
}
