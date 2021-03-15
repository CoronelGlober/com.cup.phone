package com.cup.phone.core.data.db

import com.cup.phone.core.data.db.core.newInstance
import com.cup.phone.core.data.db.core.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

interface MessagesDb : Transacter {
  val messagesQueries: MessagesQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = MessagesDb::class.schema

    operator fun invoke(driver: SqlDriver): MessagesDb = MessagesDb::class.newInstance(driver)}
}
