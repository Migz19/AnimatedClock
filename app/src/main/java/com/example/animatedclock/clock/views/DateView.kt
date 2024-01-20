package com.example.animatedclock.clock.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.example.animatedclock.R
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

private lateinit var clockBitmap: Bitmap
private lateinit var clockCanvas: Canvas
private var sweepAngle: Float = 0.0f
private lateinit var outer: RectF
private lateinit var inner: RectF
private lateinit var clockAnimator: ValueAnimator

private val thickness_scale = 0.3f
private var currentYear=0
private var currentMonth=0
private var currentDay=0
class DateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defstyleAtrr: Int = 0
) : View(context, attrs, defstyleAtrr) {
    private val clockVm:ClockViewModel by lazy { ClockViewModel() }
    var circlePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.grey, null)
        isAntiAlias = true
    }
    private val digitalTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 75f
        textAlign = Paint.Align.CENTER

    }
    private val secondaryTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER

    }
    private val tertiaryText=Paint().apply {
        color = Color.BLACK
        textSize = 35f
        textAlign = Paint.Align.CENTER
    }
    private val indicatorPaint = Paint().apply {
        color =   ContextCompat.getColor(context, R.color.grey)

        style = Paint.Style.FILL
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) /3 ).toFloat()
        val x = centerX + radius * cos(Math.toRadians(90 - sweepAngle.toDouble())).toFloat()
        val y = centerY - radius * sin(Math.toRadians(90 - sweepAngle.toDouble())).toFloat()

        if (sweepAngle > 0.0f) {
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                -90f,
                sweepAngle,
                false,
                circlePaint
            )

            canvas.drawCircle(x, y, 30f,indicatorPaint )
            canvas.drawText("$currentMonth", centerX, centerY, digitalTextPaint)
            canvas.drawText("$currentDay", x, y, tertiaryText)
            canvas.drawText("$currentYear",centerX,centerY+radius*0.5f,secondaryTextPaint)
            canvas.drawBitmap(clockBitmap, 0f, 0f, circlePaint)

        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            clockBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            clockCanvas = Canvas(clockBitmap)
        }
        super.onSizeChanged(w, h, oldw, oldh)
        modifyEdge()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clockVm.observeCurrentDay().observeForever { value ->
            currentDay=value.day
            currentMonth=value.month
            currentYear=value.year
            invalidate()
            startAnimation(currentDay)
        }


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
    fun startAnimation(days: Int) {
        val initialsweepAngle=(days*360/30f)
        clockAnimator = ValueAnimator.ofFloat(initialsweepAngle, 360f).apply {
            duration = TimeUnit.DAYS.toMillis(30-(days.toLong()))
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                    invalidate()
                }

                override fun onAnimationEnd(animation: Animator) {
                    sweepAngle = 0f }
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {
                }


            })
            addUpdateListener {

                val value = (it.animatedValue as Float)
                drawProgress(value)
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }

        clockAnimator.start()
    }

    fun setClockViewSize(width: Int, height: Int) {
        val params = layoutParams
        params.width = width
        params.height = height
        layoutParams = params
        requestLayout()
    }
}
