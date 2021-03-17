
package com.cup.phone.core.data.di

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import com.cup.phone.core.data.datasource.impl.CupPhoneClientImpl
import com.cup.phone.core.data.datasource.remote.CupPhoneClient
import com.cup.phone.core.data.db.DatabaseDriverFactory
import com.cup.phone.core.data.db.MessagesDb
import com.cup.phone.core.data.repository.MessagesRepositoryImpl
import com.cup.phone.core.domain.repository.MessagesRepository
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.Dispatchers
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object Locator {

    private const val SERVER = "18.222.68.63"
    private const val PORT = 11000
    private lateinit var messagesDb: MessagesDb
    private lateinit var repository: MessagesRepository
    private lateinit var messagesClient: CupPhoneClient


    @DangerousInternalIoApi
    @OptIn(InternalAPI::class)
    fun setUp(databaseDriverFactory: DatabaseDriverFactory) {
        this.messagesDb =  MessagesDb(databaseDriverFactory.createDriver())
        this.repository = MessagesRepositoryImpl(messagesDb)
        this.messagesClient = CupPhoneClientImpl(Dispatchers.Default, repository, SERVER, PORT)
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

public class LocatorHelper {

    private  val SERVER = "18.222.68.63"
    private  val PORT = 11000
    private lateinit var messagesDb: MessagesDb
    private lateinit var repository: MessagesRepository
    private lateinit var messagesClient: CupPhoneClient

    @DangerousInternalIoApi
    @InternalAPI
    fun setUp(databaseDriverFactory: DatabaseDriverFactory) {
        ensureNeverFrozen()
        this.messagesDb =  MessagesDb(databaseDriverFactory.createDriver())
        this.repository = MessagesRepositoryImpl(messagesDb)
        this.messagesClient = CupPhoneClientImpl(Dispatchers.Default, repository, SERVER, PORT)
    }

    @InternalAPI
    @DangerousInternalIoApi
    fun setupAux(repository: MessagesRepository) {
        this.messagesClient = CupPhoneClientImpl(Dispatchers.Default, repository, SERVER, PORT)
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