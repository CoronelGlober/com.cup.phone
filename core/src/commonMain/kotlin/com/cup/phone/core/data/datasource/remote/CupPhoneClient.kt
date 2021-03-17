package com.cup.phone.core.data.datasource.remote

import io.ktor.utils.io.*

interface CupPhoneClient {
    fun setupServer(address: String, port: Int)

    fun sendMessage(message: String)

    fun listenForMessages(input: ByteReadChannel)
}