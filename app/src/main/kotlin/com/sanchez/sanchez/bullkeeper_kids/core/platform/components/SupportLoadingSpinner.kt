package com.sanchez.sanchez.bullkeeper_kids.core.platform.components

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Support Loading Spinner
 */
class SupportLoadingSpinner: AppCompatImageView {

    private val ROTATE_ANIMATION_DURATION = 800

    /**
     * Animation
     */
    private var animation: ObjectAnimator? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?):
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)


    /**
     * On Finish Inflate
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        animation = ObjectAnimator.ofFloat(this, "rotationY", 0.0f, 360f)
        animation!!.duration = 3600
        animation!!.repeatCount = ObjectAnimator.INFINITE
        animation!!.interpolator = AccelerateDecelerateInterpolator()
        animation!!.start()
    }


    /**
     * Set Visibility
     * @param visibility
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (visibility == View.VISIBLE) {
            animation!!.start()
        } else {
            animation!!.cancel()
        }
    }
}