package com.cup.phone.android.presentation.home

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.cup.phone.android.R
import com.cup.phone.core.domain.entities.Message
import com.cup.phone.core.presentation.formatter.DateFormatter

@EpoxyModelClass(layout = R.layout.item_message)
public abstract class MessageModel : EpoxyModelWithHolder<Holder>() {
    @EpoxyAttribute
    lateinit var messageItem: Message

    override fun bind(holder: Holder) {
        holder.userNameText.text = messageItem.userName
        holder.userMessage.text = messageItem.message
        holder.cardView.setCardBackgroundColor(Color.parseColor(messageItem.userColor))
        holder.messageDate.text = DateFormatter().parseDate(messageItem.time)
    }

}

public class Holder : EpoxyHolder() {

    lateinit var userNameText: TextView
    lateinit var userMessage: TextView
    lateinit var cardView: CardView
    lateinit var messageDate: TextView


    override fun bindView(itemView: View) {
        userMessage = itemView.findViewById(R.id.message)
        userNameText = itemView.findViewById(R.id.userName)
        cardView = itemView.findViewById(R.id.cardView)
        messageDate = itemView.findViewById(R.id.time)
    }

}