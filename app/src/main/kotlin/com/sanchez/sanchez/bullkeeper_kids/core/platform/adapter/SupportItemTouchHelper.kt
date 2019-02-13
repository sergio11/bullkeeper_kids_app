package com.sanchez.sanchez.bullkeeper_kids.core.platform.adapter

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * Last Alerts Item Touch Helper
 */
class SupportItemTouchHelper<T : SupportRecyclerViewAdapter<*>.SupportItemSwipedViewHolder<*>>
/**
 * @param dragDirs
 * @param swipeDirs
 * @param listener
 */
(dragDirs: Int, swipeDirs: Int,
 private val listener: ItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    /**
     * On Move
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    /**
     * On Selected Changed
     * @param viewHolder
     * @param actionState
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as T).viewForeground
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    /**
     * On Child Draw over
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                        actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as T).viewForeground
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    /**
     * Clear View
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as T).viewForeground
        getDefaultUIUtil().clearView(foregroundView)
    }

    /**
     * On Child Draw
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                    actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as T).viewForeground
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
    }

    /**
     * On Swiped
     * @param viewHolder
     * @param direction
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }


    /**
     * Last Alerts Item Touch Helper Listener
     */
    interface ItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}