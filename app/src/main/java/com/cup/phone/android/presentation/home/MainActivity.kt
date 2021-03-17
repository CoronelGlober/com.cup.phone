package com.cup.phone.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.cup.phone.android.databinding.ActivityMainBinding
import com.cup.phone.core.data.di.Locator
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.presentation.MessagesPresenter
import com.cup.phone.core.presentation.MessagesView

class MainActivity : AppCompatActivity(), MessagesView {

    lateinit var messagesPresenter: MessagesPresenter

    lateinit var content: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        content.messagesRecycler.layoutManager = LinearLayoutManager(this, VERTICAL, true)
        messagesPresenter = MessagesPresenter(Locator.getRepository(), Locator.getClient(), this)
        messagesPresenter.startListeningForMessages(lifecycleScope)
    }


    fun sendMessage(view: View) {
        messagesPresenter.sendMessage(content.messageField.text.toString())
    }

    override fun showMessages(messages: List<Message>) {
        content.messagesRecycler.withModels {
            messages.map {
                message {
                    id(it.message)
                    messageItem(it)
                }
            }
        }
        content.messagesRecycler.scrollToPosition(0);
    }

    override fun clearMessageInput() {
        content.messageField.text.clear()
    }
}