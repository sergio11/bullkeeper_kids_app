package com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Support Recycler View Adapter
 *
 */

abstract class SupportRecyclerViewAdapter<T>
    constructor(protected var context: Context,
                private var data: MutableList<T>,
                private val minItemsCount: Int = if (data.size > 0) data.size - 1 else 0)
    : RecyclerView.Adapter<SupportRecyclerViewAdapter<T>.SupportItemViewHolder<T>>() {


    /**
     * Layout Inflater
     */
    protected var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * Listener
     */
    private var listener: OnSupportRecyclerViewListener<T>? = null

    /**
     * Highlight Text
     */
    private var highlightText: String? = null

    /**
     * Get Item
     * @param position
     * @return
     */
    fun getItem(position: Int): T {
        return data[position]
    }

    /**
     * Set data
     * @param data
     */
    fun setData(data: MutableList<T>) {
        this.data = data
    }

    /**
     * Get data
     * @return
     */
    fun getData(): List<T> {
        return this.data
    }

    /**
     * Set Highlight Text
     * @param highlightText
     */
    fun setHighlightText(highlightText: String) {
        this.highlightText = highlightText.toLowerCase()
    }

    /**
     * Has Highlight Text
     * @return
     */
    fun hasHighlightText(): Boolean {
        return highlightText != null && highlightText!!.length > 0
    }

    /**
     * Get Spannable String
     * @param text
     * @return
     */
    protected fun getSpannableString(text: String): Spannable {
        val lowerText = text.toLowerCase()
        val spannable = SpannableString(lowerText)
        val startIndex = lowerText.indexOf(highlightText!!)
        if (startIndex >= 0) {
            val stopIndex = startIndex + highlightText!!.length
            spannable.setSpan(ForegroundColorSpan(Color.YELLOW), startIndex, stopIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannable
    }


    /**
     * Get Item by adapter position
     * @return
     */
    fun getItemByAdapterPosition(position: Int): T {
        return data[position]
    }


    /**
     * Get Item Count
     * @return
     */
    override fun getItemCount(): Int = Math.max(data.size, minItemsCount)

    /**
     * Get Item id
     * @param position
     * @return
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * On Create View Holder
     * @param viewGroup
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):
            SupportItemViewHolder<T> = onCreateItemViewHolder(viewGroup)

    override fun onBindViewHolder(holder: SupportItemViewHolder<T>, position: Int) {
        holder.bind(data[position])
    }

    /**
     * On Create Item View Holder
     * @param viewGroup
     */
    protected abstract fun onCreateItemViewHolder(viewGroup: ViewGroup): SupportItemViewHolder<T>

    /**
     * Remove Item
     * @param position
     */
    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Restore Item
     * @param item
     * @param position
     */
    fun restoreItem(item: T, position: Int) {
        data.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }

    /**
     * Remove All
     */
    fun removeAll() {
        notifyItemRangeRemoved(0, data.size)
    }

    /**
     * Set On Support Recycler View Listener
     */
    fun setOnSupportRecyclerViewListener(listener: OnSupportRecyclerViewListener<T>) {
        this.listener = listener
    }

    /**
     * On Support Recycler View Listener
     */
    interface OnSupportRecyclerViewListener<T> {
        fun onItemClick(item: T)
    }


    /**
     * View Holders Types
     */

    /**
     * Support Item View Holder
     * @param <T>
    </T> */
    abstract inner class SupportItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        open fun bind(element: T) {
            if (listener != null) {
                itemView.setOnClickListener { listener!!.onItemClick(data[layoutPosition]) }
            }
        }
    }


}
