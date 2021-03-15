package com.cup.phone.core.data.dto

import kotlinx.serialization.*

@Serializable
public data class RawMessage(
    val userName: String,
    val userColor: String,
    val message: String,
    val timestamp: Long
)
