package com.cup.phone.core.data.db

import kotlin.Long
import kotlin.String

data class Messages(
  val key: Long,
  val timestamp: Long,
  val userName: String,
  val userColor: String,
  val message: String
) {
  override fun toString(): String = """
  |Messages [
  |  key: $key
  |  timestamp: $timestamp
  |  userName: $userName
  |  userColor: $userColor
  |  message: $message
  |]
  """.trimMargin()
}
