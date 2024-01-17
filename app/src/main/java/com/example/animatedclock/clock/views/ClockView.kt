package com.example.animatedclock.clock.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.concurrent.TimeUnit

private lateinit var clockBitmap: Bitmap
private lateinit var clockCanvas: Canvas
private var sweepAngle: Float = 0.0f
private lateinit var outer: RectF
private lateinit var inner: RectF
private lateinit var clockAnimator: ValueAnimator
private lateinit var circlePaint: Paint
private lateinit var eraserPaint: Paint
private val thickness_scale = 0.3f
private val arc_angle = 270f

class ClockView(context: Context) : View(context) {
    constructor(Context: Context, attrs: AttributeSet) : this(Context)
    constructor(Context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(Context)

    init {
        circlePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 50f
            strokeCap = Paint.Cap.ROUND
            color = resources.getColor(android.R.color.holo_blue_dark, null)
            isAntiAlias = true
        }
        eraserPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        clockAnimator = ValueAnimator.ofFloat(0.0f, 360.0f).apply {
            duration = 10000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawColor(0, PorterDuff.Mode.CLEAR)
            if (sweepAngle > 0.0f) {
                drawArc(outer, arc_angle, sweepAngle, true, circlePaint)
                drawOval(inner, eraserPaint)
            }
            drawBitmap(clockBitmap, 0f, 0f, null)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //check the following code for more details
        //https://stackoverflow.com/questions/15261088/android-canvas-drawbitmap-not-working
        if (w != oldw || h != oldh) {
            clockBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            clockBitmap.eraseColor(Color.TRANSPARENT)
            clockCanvas = Canvas(clockBitmap)
        }
        super.onSizeChanged(w, h, oldw, oldh)
        modifyEdge()
    }

    fun startAnimation( seconds:Int) {
        clockAnimator= ValueAnimator.ofFloat(0.0f, 1f).apply {
            duration = TimeUnit.SECONDS.toMillis(seconds.toLong())
            repeatCount = ValueAnimator.INFINITE
            interpolator=LinearInterpolator()

            addUpdateListener {
                drawProgress(it.animatedValue as Float)
                invalidate()
            }
        }
        clockAnimator.start()
    }

     fun stopAnimation() {
        clockAnimator.cancel()
        drawProgress(0f)
    }

    private fun drawProgress(progress: Float) {
        sweepAngle = 360 * progress
        invalidate()
    }

    private fun modifyEdge() {
        val thickness = width * thickness_scale
        outer = RectF(0f, 0f, width.toFloat(), height.toFloat())
        inner = RectF(outer.left + thickness, outer.top + thickness, outer.right + thickness, outer.bottom + thickness)
        invalidate()
    }
}