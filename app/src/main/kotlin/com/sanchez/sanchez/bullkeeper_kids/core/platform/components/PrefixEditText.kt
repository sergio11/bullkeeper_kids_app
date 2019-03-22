package com.sanchez.sanchez.bullkeeper_kids.core.platform.components

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet

class PrefixEditText : AppCompatEditText {
    internal var mOriginalLeftPadding = -1f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(widthMeasureSpec: Int,
                           heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculatePrefix()
    }

    /**
     * Calculate Prefix
     */
    private fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            val prefix = tag as String
            val widths = FloatArray(prefix.length)
            paint.getTextWidths(prefix, widths)
            var textWidth = 0f
            for (w in widths) {
                textWidth += w
            }
            mOriginalLeftPadding = compoundPaddingLeft.toFloat()
            setPadding((textWidth + mOriginalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val prefix = tag as String
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null).toFloat(), paint)
    }
}