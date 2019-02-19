package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter.SupportRecyclerViewAdapter
import com.sanchez.sanchez.bullkeeper_kids.domain.models.GuardianRolesEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import com.squareup.picasso.Picasso
import java.util.*

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
        val view = inflater.inflate(R.layout.kid_guardian_item_layout, viewGroup, false)
        return KidGuardianViewHolder(view)

    }

    /**
     * Kid Guardian View Holder
     */
    inner class KidGuardianViewHolder(itemView: View) : SupportRecyclerViewAdapter<KidGuardianEntity>
        .SupportItemViewHolder<KidGuardianEntity>(itemView) {

        /**
         * Bind
         */
        @SuppressLint("SetTextI18n")
        override fun bind(element: KidGuardianEntity) {
            super.bind(element)

            itemView.findViewById<TextView>(R.id.userName).text =
                    String.format(Locale.getDefault(), "%s %s",
                            element.guardian?.firstName, element.guardian?.lastName)

            if(!element.guardian?.profileImage.isNullOrEmpty()) {
                picasso.load(element.guardian?.profileImage!!)
                        .placeholder(R.drawable.user_default_inverse_solid)
                        .error(R.drawable.user_default_inverse_solid)
                        .into(itemView.findViewById<ImageView>(R.id.userImage))
            } else {
                itemView.findViewById<ImageView>(R.id.userImage)
                        .setImageResource(R.drawable.user_default_inverse_solid)
            }

            itemView.findViewById<TextView>(R.id.roleDescription).text =
                    element.role?.let {
                        when(it) {
                            GuardianRolesEnum.DATA_VIEWER ->
                                context.getString(R.string.kid_guardian_data_viewer_role)
                            GuardianRolesEnum.PARENTAL_CONTROL_RULE_EDITOR ->
                                context.getString(R.string.kid_guardian_parental_control_rule_editor_role)
                            GuardianRolesEnum.ADMIN ->
                                context.getString(R.string.kid_guardian_admin_role)
                        }
                    } ?: context.getString(R.string.kid_guardian_data_viewer_role)
        }
    }


}