package com.example.animatedclock.clock.views

import com.example.animatedclock.R

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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
private var currentHour=0
private var am_pm=""
private var totalMinutes=0
class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defstyleAtrr: Int = 0
) : View(context, attrs, defstyleAtrr) {
    private val clockVm:ClockViewModel by lazy { ClockViewModel() }
    var circlePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 25f
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.customCyan, null)
        isAntiAlias = true
    }
    private val digitalTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 150f
        textAlign = Paint.Align.CENTER

    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }
    private val indicatorPaint = Paint().apply {
        color =   ContextCompat.getColor(context, R.color.customPink)

        style = Paint.Style.FILL
    }
    private val pointPaint=Paint().apply {
        color = ContextCompat.getColor(context, R.color.grey)

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) /2*0.9 ).toFloat()
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
            for (minute in totalMinutes until 60) {
                val fraction = minute / 360f
                val sweepAngle = 360-(fraction * 60f)
                val x = centerX + radius * cos(sweepAngle)
                val y = centerY + radius * sin(sweepAngle)
                // Draw circular point

                canvas.drawCircle(x, y,4f, pointPaint)

            }
            canvas.drawCircle(x, y, 50f,indicatorPaint )

            canvas.drawText("$currentHour", centerX, centerY+(radius*0.3f), digitalTextPaint)

            canvas.drawText(am_pm,centerX+(radius* cos(Math.toRadians(60.0)).toFloat()),centerY-(radius* 0.1.toFloat()),textPaint)
            canvas.drawText(totalMinutes.toString(),x,y+10,textPaint)
            canvas.drawBitmap(clockBitmap, 0f, 0f, circlePaint)

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //check the following code for more details
        if (w != oldw || h != oldh) {
            clockBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            clockCanvas = Canvas(clockBitmap)
        }
        super.onSizeChanged(w, h, oldw, oldh)
        modifyEdge()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clockVm.observeMinutesValue().observeForever { value ->

            totalMinutes=value
            invalidate()
            startAnimation(value)
        }
        clockVm.observeHoursValue().observeForever{
            currentHour=it.first
            am_pm=it.second
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

    ///////////////////////////Animation//////////////////////////////


    fun startAnimation(minutes: Int) {
        val initialsweepAngle=(minutes*360/60f)
        clockAnimator = ValueAnimator.ofFloat(initialsweepAngle, 360f).apply {
            duration = TimeUnit.MINUTES.toMillis(60-(minutes.toLong()))
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
                val gradientColors = intArrayOf(
                    ContextCompat.getColor(context, R.color.customCyan),
                    ContextCompat.getColor(context, R.color.customMagneta),
                    ContextCompat.getColor(context, R.color.customPink),
                    ContextCompat.getColor(context, R.color.customCyan)
                )
                val gradientPositions = floatArrayOf(0f, 0.25f, 0.5f, 1f)
                val sweepGradient=setSweepAnim(gradientColors,gradientPositions)

                circlePaint.shader = sweepGradient

                val value = (it.animatedValue as Float)
                drawProgress(value)
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }

        clockAnimator.start()
    }
    private fun setSweepAnim (colors:IntArray, positions:FloatArray): SweepGradient {
        val sweepGradient = SweepGradient(
            width.toFloat() / 2, height.toFloat() / 2,
            colors, positions
        )
        val matrix = Matrix()
        matrix.setRotate(-90f, width.toFloat() / 2, height.toFloat() / 2)
        sweepGradient.setLocalMatrix(matrix)
        return sweepGradient
    }

}