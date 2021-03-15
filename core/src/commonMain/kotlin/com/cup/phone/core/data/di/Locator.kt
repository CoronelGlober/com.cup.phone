package com.cup.phone.core.data.di

import com.cup.phone.core.data.datasource.impl.CupPhoneClientImpl
import com.cup.phone.core.data.datasource.remote.CupPhoneClient
import com.cup.phone.core.data.db.DatabaseDriverFactory
import com.cup.phone.core.data.db.MessagesDb
import com.cup.phone.core.data.repository.MessagesRepositoryImpl
import com.cup.phone.core.domain.repository.MessagesRepository
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers

object Locator {

    private const val SERVER = "18.222.68.63"
    private const val PORT = 11000
    private lateinit var messagesDb: MessagesDb
    private lateinit var repository: MessagesRepository
    private lateinit var messagesClient: CupPhoneClient


    @OptIn(InternalAPI::class)
    fun setUp(databaseDriverFactory: DatabaseDriverFactory) {
        messagesDb = MessagesDb(databaseDriverFactory.createDriver())
        repository = MessagesRepositoryImpl(messagesDb)
        messagesClient = CupPhoneClientImpl(Dispatchers.Default, repository, SERVER, PORT)
    }

    fun getDB(): MessagesDb {
        return messagesDb
    }

    fun getRepository(): MessagesRepository {
        return repository
    }

    fun getClient(): CupPhoneClient {
        return messagesClient
    }
}