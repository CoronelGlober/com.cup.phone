package com.cup.phone.android.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.airbnb.epoxy.EpoxyRecyclerView
import com.cup.phone.android.R
import com.cup.phone.android.databinding.ActivityMainBinding
import com.cup.phone.core.data.di.Locator
import com.cup.phone.core.presentation.MessagesPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var messagesPresenter: MessagesPresenter

    lateinit var content: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        content.messagesRecycler.layoutManager = LinearLayoutManager(this, VERTICAL, true)
        messagesPresenter = MessagesPresenter(Locator.getRepository(), Locator.getClient())

        lifecycleScope.launchWhenResumed {
            messagesPresenter.getMessages()
                .catch { println(it.message) }
                .collect { elements ->
                    withContext(Dispatchers.Main) {
                        content.messagesRecycler.setModels(
                            elements.map {
                                MessageModel_()
                                    .id(it.message)
                                    .messageItem(it)
                            }
                        )
                        content.messagesRecycler.scrollToPosition(0);
                    }
                }
        }
    }


    fun sendMessage(view: View) {
        messagesPresenter.sendMessage(content.messageField.text.toString())
        content.messageField.text.clear()
    }
}