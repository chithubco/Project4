package com.echithub.project4.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.echithub.project4.R

private enum class DownloadState(val label: Int){
    OFF(R.string.download_off),
    ON(R.string.downloading),
    COMPLETED(R.string.download_completed);

    fun next() = when(this){
        OFF -> ON
        ON -> COMPLETED
        COMPLETED -> OFF
    }
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private lateinit var frame: Rect
    private var downloadStatus = DownloadState.OFF
    private var barWidth = 1
    private var buttonText = "Download"

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("",Typeface.BOLD)
        color = Color.RED
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create("",Typeface.NORMAL)
    }
    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        if (super.performClick()) {
            downloadStatus = DownloadState.ON
            Log.i("Download Status",downloadStatus.toString())
            for (x in 2..660){
                computeBarPosition(downloadStatus, width)
                invalidate()
                onSizeChanged(width,height,width,height)
            }
            return true
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas.drawRect(frame, paint)
        canvas.drawText(buttonText, 320F,80F,textPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        val inset = 1
        frame = Rect(inset, inset,barWidth, height - inset)
    }

    private fun computeBarPosition(pos: DownloadState, width: Int){
        if (pos == DownloadState.ON){
            if (barWidth <= width){
                buttonText = "Downloading......."
                downloadStatus = DownloadState.COMPLETED
            }
            barWidth += 1

        }else if (pos == DownloadState.OFF){
            barWidth = 0
            buttonText = "Download"
        }else if (pos == DownloadState.COMPLETED){
            barWidth = width
            downloadStatus = DownloadState.COMPLETED
            buttonText = "Completed"
        }
    }
}