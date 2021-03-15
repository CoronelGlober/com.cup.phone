package com.cup.phone.core.presentation

import com.cup.phone.core.data.datasource.remote.CupPhoneClient
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

class MessagesPresenter(val repository: MessagesRepository, val messagesClient: CupPhoneClient) {

    fun getMessages(): Flow<List<Message>> {
        return repository.getMessages()
    }

    fun sendMessage(message: String) {
        messagesClient.sendMessage(message)
    }
}