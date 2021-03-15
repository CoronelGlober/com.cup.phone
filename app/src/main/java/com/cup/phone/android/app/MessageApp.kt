package com.cup.phone.android.app

import android.app.Application
import com.cup.phone.core.data.db.DatabaseDriverFactory
import com.cup.phone.core.data.di.Locator

class MessageApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Locator.setUp(DatabaseDriverFactory(this))
    }
}