package com.cup.phone.core.domain.repository

import com.cup.phone.core.data.dto.RawMessage
import com.cup.phone.core.data.repository.CommonFlow
import com.cup.phone.core.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun getMessages(): Flow<List<Message>>
    fun getMessagesHelper(): CommonFlow<List<Message>>
    fun addMessage(message: RawMessage)
}