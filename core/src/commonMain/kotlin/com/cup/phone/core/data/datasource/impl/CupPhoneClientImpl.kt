package com.cup.phone.core.data.datasource.impl

import com.cup.phone.core.ApplicationDispatcher
import com.cup.phone.core.data.datasource.remote.CupPhoneClient
import com.cup.phone.core.data.dto.RawMessage
import com.cup.phone.core.domain.repository.MessagesRepository
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import io.ktor.network.selector.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.*
import kotlin.native.concurrent.ThreadLocal

@DangerousInternalIoApi
@InternalAPI
class CupPhoneClientImpl(
    private val dispatcher: CoroutineDispatcher,
    private val messagesRepository: MessagesRepository, address: String, port: Int
) : CupPhoneClient {
    init {
        setupServer(address, port)
    }
//    var input: ByteReadChannel? = null
    var output: ByteWriteChannel? = null
    lateinit var currentJob: Job

    @DangerousInternalIoApi
    @InternalAPI
    override fun setupServer(address: String, port: Int) {
        currentJob = GlobalScope.launch(ApplicationDispatcher) {
            val socket = aSocket(SelectorManager(ApplicationDispatcher)).tcp()
                .connect(NetworkAddress(address, port))
            val input = socket.openReadChannel()
            output = socket.openWriteChannel(autoFlush = true)
            listenForMessages(input)
        }
    }

    override fun sendMessage(message: String) {
        GlobalScope.launch(ApplicationDispatcher) {
            val messageToSend = (message + "\n").toByteArray()
            output?.writeAvailable(
                messageToSend,
                0,
                messageToSend.size
            )
        }
    }

    override fun listenForMessages(input: ByteReadChannel) {
        GlobalScope.launch(ApplicationDispatcher) {
            while (!currentJob.isCancelled) {
                input.readUTF8Line(Int.MAX_VALUE)?.let { message ->
                    val rawMessage = Json.decodeFromString<RawMessage>(message)
                    messagesRepository.addMessage(rawMessage)
                }
            }
        }
    }
}