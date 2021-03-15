package com.cup.phone.core.data.datasource.remote

interface CupPhoneClient {
    fun setupServer(address: String, port: Int)

    fun sendMessage(message: String)

    fun listenForMessages()
}