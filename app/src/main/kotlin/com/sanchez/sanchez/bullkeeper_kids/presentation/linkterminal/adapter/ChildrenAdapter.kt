package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SupervisedChildrenEntity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Children Adapter
 */
class ChildrenAdapter
    constructor(
            context: Context,
            data: MutableList<SupervisedChildrenEntity>,
            private val piccaso: Picasso
    ): SupportRecyclerViewAdapter<SupervisedChildrenEntity>(context, data) {


    /**
     * On Create Item View Holder
     */
    override fun onCreateItemViewHolder(viewGroup: ViewGroup): SupportItemViewHolder<SupervisedChildrenEntity> {
        val view = inflater.inflate(R.layout.children_item_layout, viewGroup, false)
        return ChildrenViewHolder(view)

    }

    /**
     * Children View Holder
     */
    inner class ChildrenViewHolder(itemView: View) : SupportRecyclerViewAdapter<SupervisedChildrenEntity>
    .SupportItemViewHolder<SupervisedChildrenEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: SupervisedChildrenEntity) {
            super.bind(element)

            val kidFullName =
                    itemView.findViewById<TextView>(R.id.kidFullNameTextView)

            // Set Kid Full Name
            kidFullName.text = String.format(context.getString(R.string.child_full_name_detail),
                    element.kid?.firstName, element.kid?.lastName, element.kid?.age)


            val schoolName =
                    itemView.findViewById<TextView>(R.id.schoolNameTextView)

            // Set School Name
            schoolName.text = element.kid?.school?.name


            val terminalsLinked =
                    itemView.findViewById<TextView>(R.id.terminalsLinkedTextView)

            // Set Terminals Linked
            if(element.kid?.terminals?.isNotEmpty()!!)
                terminalsLinked.text = String.format(
                        context.getString(R.string.child_has_terminals_linked),
                        element.kid?.terminals?.size)
            else
                terminalsLinked.text = context.getString(R.string.child_has_not_terminals_linked)


            val childImageImageView =
                    itemView.findViewById<CircleImageView>(R.id.childImageImageView)

            // Set Profile Image
            piccaso.load(element.kid?.profileImage)
                    .placeholder(R.drawable.child_white_solid)
                    .error(R.drawable.child_white_solid)
                    .into(childImageImageView)


        }
    }


}