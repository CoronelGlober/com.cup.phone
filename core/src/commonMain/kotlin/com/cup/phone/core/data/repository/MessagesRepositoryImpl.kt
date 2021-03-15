package com.cup.phone.core.data.repository

import com.cup.phone.core.data.db.MessagesDb
import com.cup.phone.core.data.dto.RawMessage
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.domain.repository.MessagesRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MessagesRepositoryImpl(private val messagesDb: MessagesDb) : MessagesRepository {
    override fun getMessages(): Flow<List<Message>> {
        return messagesDb.messagesQueries.selectAll(::messageMapper).asFlow().mapToList()
    }

    override fun addMessage(message: RawMessage) {
        messagesDb.messagesQueries.insertMessage(
            message.timestamp,
            message.userName,
            message.userColor,
            message.message
        )
    }

    private fun messageMapper(
        key: Long,
        timestamp: Long,
        userName: String,
        userColor: String,
        message: String
    ): Message {
        return Message(userName, userColor, message)
    }
}