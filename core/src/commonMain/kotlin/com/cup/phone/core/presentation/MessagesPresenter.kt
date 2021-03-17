package com.cup.phone.core.presentation

import com.cup.phone.core.ApplicationDispatcher
import com.cup.phone.core.data.datasource.remote.CupPhoneClient
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.domain.repository.MessagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessagesPresenter(
    val repository: MessagesRepository,
    val messagesClient: CupPhoneClient,
    val view: MessagesView
) {

    fun startListeningForMessages() {
        GlobalScope.launch(ApplicationDispatcher) {
            repository.getMessages().collect {
                view.showMessages(it)
            }
        }
    }

    fun sendMessage(message: String) {
        messagesClient.sendMessage(message)
        view.clearMessageInput()
    }
}