package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.invisible
import com.sanchez.sanchez.bullkeeper_kids.core.extension.visible
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Conversation Adapter
 */
class ConversationAdapter
    constructor(
            context: Context,
            data: MutableList<ConversationEntity>,
            private val picasso: Picasso,
            private val selfUserId: String
    ): SupportRecyclerViewAdapter<ConversationEntity>(context, data) {


    /**
     * On Create Item View Holder
     */
    override fun onCreateItemViewHolder(viewGroup: ViewGroup): SupportItemViewHolder<ConversationEntity> {
        val view = inflater.inflate(R.layout.conversation_item_layout, viewGroup, false)
        return ConversationViewHolder(view)

    }

    /**
     * Conversation View Holder
     */
    inner class ConversationViewHolder(itemView: View) : SupportRecyclerViewAdapter<ConversationEntity>
    .SupportItemSwipedViewHolder<ConversationEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: ConversationEntity) {
            super.bind(element)

            val userTarget = if(element.memberOne?.identity == selfUserId)
                element.memberTwo
            else
                element.memberOne

            picasso.load(userTarget?.profileImage)
                    .placeholder(R.drawable.user_default)
                    .error(R.drawable.user_default)
                    .into(itemView.findViewById<ImageView>(R.id.userImage))

            itemView.findViewById<TextView>(R.id.userName).text =
                    String.format(Locale.getDefault(), "%s - %s",
                            userTarget?.firstName, userTarget?.lastName)

            itemView.findViewById<TextView>(R.id.lastMessage).text =
                    element.lastMessage

            val unreadMessagesCountTextView =
                    itemView.findViewById<TextView>(R.id.unreadMessagesCount)

            if(element.unreadMessages > 0) {
                unreadMessagesCountTextView.visible()
                unreadMessagesCountTextView.text = element.unreadMessages.toString()
            } else {
                unreadMessagesCountTextView.invisible()
            }
        }
    }


}