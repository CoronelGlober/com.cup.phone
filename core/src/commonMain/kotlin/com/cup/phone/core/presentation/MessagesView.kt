package com.cup.phone.core.presentation

import com.cup.phone.core.domain.entities.Message

interface MessagesView {
    fun showMessages(message: List<Message>)

    fun clearMessageInput()
}