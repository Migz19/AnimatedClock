package com.example.animatedclock.clock.views

import android.R
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import java.lang.StrictMath.cos
import java.util.concurrent.TimeUnit
import kotlin.math.sin

private lateinit var clockBitmap: Bitmap
private lateinit var clockCanvas: Canvas
private var sweepAngle: Float = 0.0f
private lateinit var outer: RectF
private lateinit var inner: RectF
private lateinit var clockAnimator: ValueAnimator

private val thickness_scale = 0.3f


class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defstyleAtrr: Int = 0
) : View(context, attrs, defstyleAtrr) {
    var circlePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.holo_blue_dark, null)
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) / 2 ).toFloat()

        if (sweepAngle > 0.0f) {
            circlePaint.shader
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

            canvas.drawBitmap(clockBitmap, 0f, 0f, circlePaint)

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //check the following code for more details
        //https://stackoverflow.com/questions/15261088/android-canvas-drawbitmap-not-working
        if (w != oldw || h != oldh) {
            clockBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            // clockBitmap.eraseColor(Color.TRANSPARENT)
            clockCanvas = Canvas(clockBitmap)
        }
        super.onSizeChanged(w, h, oldw, oldh)
        modifyEdge()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

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

        clockAnimator = ValueAnimator.ofFloat(0.0f, 360f).apply {
            duration = TimeUnit.SECONDS.toMillis(minutes.toLong())
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    sweepAngle = 0f }
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {
                }


            })
            addUpdateListener {

                val gradientColors = intArrayOf(
                    evaluateColor(0f, Color.argb(255, 0, 139, 255)),
                    evaluateColor(0f, Color.argb(255, 121, 91, 173)),
                    evaluateColor(0f, Color.argb(255, 220, 40, 106))
                )
                circlePaint.shader = LinearGradient(
                    2.5f, 0f, width.toFloat(), height.toFloat(),
                    gradientColors, null, Shader.TileMode.CLAMP
                )
                val value=(it.animatedValue as Float )
                drawProgress(value)
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }

        clockAnimator.start()
    }

    fun stopAnimation() {
        clockAnimator.cancel()
        drawProgress(0f)
    }

    private fun evaluateColor(fraction: Float, startValue: Int): Int {
        val startA = Color.alpha(startValue)
        val startR = Color.red(startValue)
        val startG = Color.green(startValue)
        val startB = Color.blue(startValue)

        return Color.argb(
            (startA + fraction * (255 - startA)).toInt(),
            (startR + fraction * (255 - startR)).toInt(),
            (startG + fraction * (255 - startG)).toInt(),
            (startB + fraction * (255 - startB)).toInt()
        )
    }
}