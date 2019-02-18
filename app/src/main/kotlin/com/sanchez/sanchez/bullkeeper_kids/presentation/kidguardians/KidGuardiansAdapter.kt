package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import com.squareup.picasso.Picasso

/**
 * Kid Guardians Adapter
 */
class KidGuardiansAdapter
    constructor(
            context: Context,
            data: MutableList<KidGuardianEntity>,
            private val picasso: Picasso
    ): SupportRecyclerViewAdapter<KidGuardianEntity>(context, data) {


    /**
     * On Create Item View Holder
     */
    override fun onCreateItemViewHolder(viewGroup: ViewGroup): SupportItemViewHolder<KidGuardianEntity> {
        val view = inflater.inflate(R.layout.conversation_item_layout, viewGroup, false)
        return ConversationViewHolder(view)

    }

    /**
     * Conversation View Holder
     */
    inner class ConversationViewHolder(itemView: View) : SupportRecyclerViewAdapter<KidGuardianEntity>
    .SupportItemViewHolder<KidGuardianEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: KidGuardianEntity) {
            super.bind(element)


        }
    }


}