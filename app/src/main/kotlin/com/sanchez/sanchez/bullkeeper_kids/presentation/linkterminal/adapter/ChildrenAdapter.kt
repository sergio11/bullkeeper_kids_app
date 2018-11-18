package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SonEntity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Children Adapter
 */
class ChildrenAdapter
    constructor(
            context: Context,
            data: MutableList<SonEntity>,
            private val piccaso: Picasso
    ): SupportRecyclerViewAdapter<SonEntity>(context, data) {


    /**
     * On Create Item View Holder
     */
    override fun onCreateItemViewHolder(viewGroup: ViewGroup): SupportItemViewHolder<SonEntity> {
        val view = inflater.inflate(R.layout.children_item_layout, viewGroup, false)
        return ChildrenViewHolder(view)

    }

    /**
     * Children View Holder
     */
    inner class ChildrenViewHolder(itemView: View) : SupportRecyclerViewAdapter<SonEntity>
    .SupportItemViewHolder<SonEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: SonEntity) {
            super.bind(element)

            val kidFullName =
                    itemView.findViewById<TextView>(R.id.kidFullNameTextView)

            // Set Kid Full Name
            kidFullName.text = String.format(context.getString(R.string.child_full_name_detail),
                    element.firstName, element.lastName, element.age)


            val schoolName =
                    itemView.findViewById<TextView>(R.id.schoolNameTextView)

            // Set School Name
            schoolName.text = element.school?.name


            val terminalsLinked =
                    itemView.findViewById<TextView>(R.id.terminalsLinkedTextView)

            // Set Terminals Linked
            if(element.terminals?.isNotEmpty()!!)
                terminalsLinked.text = String.format(
                        context.getString(R.string.child_has_terminals_linked),
                        element.terminals?.size)
            else
                terminalsLinked.text = context.getString(R.string.child_has_not_terminals_linked)


            val childImageImageView =
                    itemView.findViewById<CircleImageView>(R.id.childImageImageView)

            // Set Profile Image
            piccaso.load(element.profileImage)
                    .placeholder(R.drawable.child_white_solid)
                    .error(R.drawable.child_white_solid)
                    .into(childImageImageView)


        }
    }


}