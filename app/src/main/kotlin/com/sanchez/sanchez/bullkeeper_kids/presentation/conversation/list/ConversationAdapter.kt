package com.sanchez.sanchez.bullkeeper_kids.presentation.conversation.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ConversationEntity
import com.squareup.picasso.Picasso

/**
 * Conversation Adapter
 */
class ConversationAdapter
    constructor(
            context: Context,
            data: MutableList<ConversationEntity>,
            private val piccaso: Picasso
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
    .SupportItemViewHolder<ConversationEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: ConversationEntity) {
            super.bind(element)


        }
    }


}