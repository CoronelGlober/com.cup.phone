package com.cup.phone.core.data.repository

import com.cup.phone.core.data.db.MessagesDb
import com.cup.phone.core.data.dto.RawMessage
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.domain.repository.MessagesRepository
import com.squareup.sqldelight.db.Closeable
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class MessagesRepositoryImpl(private val messagesDb: MessagesDb) : MessagesRepository {
    override fun getMessages(): Flow<List<Message>> {
        return messagesDb.messagesQueries.selectAll(::messageMapper).asFlow().mapToList()
    }

    override fun getMessagesHelper(): CommonFlow<List<Message>> {
        return messagesDb.messagesQueries.selectAll(::messageMapper)
            .asFlow()
            .mapToList()
            .asCommonFlow()
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
        return Message(timestamp,userName, userColor, message)
    }
}

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)
class CommonFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}