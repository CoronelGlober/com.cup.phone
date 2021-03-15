package com.cup.phone.core.data.db

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface MessagesQueries : Transacter {
  fun <T : Any> selectAll(mapper: (
    key: Long,
    timestamp: Long,
    userName: String,
    userColor: String,
    message: String
  ) -> T): Query<T>

  fun selectAll(): Query<Messages>

  fun insertMessage(
    timestamp: Long,
    userName: String,
    userColor: String,
    message: String
  )

  fun deleteAll()
}
