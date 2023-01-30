package com.araa.dravio.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.araa.dravio.domain.Stroke
import kotlin.properties.Delegates

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val TOUCH_TOLERANCE: Float = 4.0F
    }

    var mX by Delegates.notNull<Float>()
    var mY by Delegates.notNull<Float>()
    lateinit var path: Path
    var mPaint: Paint = Paint()
    private var paths: ArrayList<Stroke> = arrayListOf()
    private var currentColor: Int = 0
    private var strokeWidth: Int = 0
    var mBitmap: Bitmap? = null
    var mCanvas: Canvas? = null
    var mMapPaint: Paint = Paint(Paint.DITHER_FLAG)
    var params: ViewGroup.LayoutParams? = null


    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.MAGENTA
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.alpha = 0xff

        params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mBitmap = params?.let { Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888) }
        mCanvas = mBitmap?.let { Canvas(it) }

        currentColor = Color.MAGENTA
        strokeWidth = 20
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    fun setColor(color: Int) {
        currentColor = color
    }

    fun strokeWidth(width: Int) {
        strokeWidth = width
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    fun save(): Bitmap? {
        return mBitmap
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        val backgroundColor = Color.WHITE
        mCanvas?.drawColor(backgroundColor)

        paths.forEach {
            mPaint.color = it.color
            mPaint.strokeWidth = it.strokeWidth.toFloat()
            mCanvas?.drawPath(it.path, mPaint)
        }
    }

    private fun touchStart(x: Float, y: Float) {
        path = Path()
        val strk = Stroke(currentColor, strokeWidth, path)
        paths.add(strk)
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx: Float = Math.abs(x - mX)
        val dy: Float = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        path.lineTo(mX, mY)
    }


}