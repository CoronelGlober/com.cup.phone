package com.cup.phone.android.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.cup.phone.android.R
import com.cup.phone.core.data.di.Locator
import com.cup.phone.core.presentation.MessagesPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var messagesPresenter: MessagesPresenter
    lateinit var messageField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<EpoxyRecyclerView>(R.id.messagesRecycler)
        recycler.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, true)
        messagesPresenter = MessagesPresenter(Locator.getRepository(), Locator.getClient())
        messageField = findViewById<EditText>(R.id.messageField)

        lifecycleScope.launchWhenResumed {
            messagesPresenter.getMessages().collect { elements ->
                withContext(Dispatchers.Main) {
                    recycler.setModels(
                        elements.map {
                            MessageModel_()
                                .id(it.message)
                                .messageItem(it)
                        }
                    )
                    recycler.scrollToPosition(0);
                }
            }
        }
    }


    fun sendMessage(view: View) {
        messagesPresenter.sendMessage(messageField.text.toString())
        messageField.text.clear()
    }
}