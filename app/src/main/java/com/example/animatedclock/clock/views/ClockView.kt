package com.example.animatedclock.clock.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import androidx.lifecycle.MutableLiveData
import com.example.animatedclock.R
import com.example.animatedclock.weather.data.WeatherData
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

private lateinit var clockBitmap: Bitmap
private lateinit var clockCanvas: Canvas
private var sweepAngle: Float = 0.0f
private lateinit var outer: RectF
private lateinit var inner: RectF
private lateinit var clockAnimator: ValueAnimator
private  var thickness_scale =0f
private var textColor=Color.BLACK
private var indicatorColor=Color.BLACK
private var dotsColor=Color.BLACK
private var currentHour = 0
private var am_pm = ""
private var strokeWidth=0f
private var totalMinutes = 0
private var temperature = 0.0
private var textSize=0f
private var colorsArray: IntArray= IntArray(0)
class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defstyleAtrr: Int = 0,
) : View(context, attrs, defstyleAtrr) {
    private val clockVm: ClockViewModel by lazy { ClockViewModel() }
    private var circlePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = this.strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = resources.getColor(R.color.customCyan, null)
        isAntiAlias = true
    }
    private val digitalTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 100f
        textAlign = Paint.Align.CENTER

    }

    private val textPaint = Paint().apply {
        color = textColor
        textSize = this.textSize
        textAlign = Paint.Align.CENTER
    }
    private val indicatorPaint = Paint().apply {
        color = indicatorColor
        style = Paint.Style.FILL
    }
    private val pointPaint = Paint().apply {
        color = dotsColor
    }

    override fun requestLayout() {
        super.requestLayout()
        observeDataChanges()
        weatherData.observeForever {
            temperature = it.temperature_2m
            invalidate()
        }

    }

    init {
        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomClockView)
            val typedValue= TypedValue()
            resources.getValue(R.dimen.clockThickness,typedValue,true)
            thickness_scale=typedValue.float
            resources.getValue(R.dimen.clockStrokeWidth,typedValue,true)
            strokeWidth=typedValue.float
            textColor=typedArray.getColor(R.styleable.CustomClockView_textColor,Color.BLACK)
            resources.getValue(R.dimen.clockTextSize,typedValue,true)
            textSize=typedValue.float
            indicatorColor=typedArray.getColor(R.styleable.CustomClockView_indicatorColor,Color.BLACK)
            dotsColor=typedArray.getColor(R.styleable.CustomClockView_dotsColor,Color.BLACK)
            colorsArray[0]=typedArray.getColor(R.styleable.CustomClockView_colorOnPrimary,Color.BLACK)
            colorsArray[1]=typedArray.getColor(R.styleable.CustomClockView_colorOnSecondary, colorsArray[0])
            colorsArray[2]=typedArray.getColor(R.styleable.CustomClockView_colorOnTertiary, colorsArray[1])
            colorsArray[3]= colorsArray[0]
            typedArray.recycle()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) / 3).toFloat()
        val x = centerX + radius * cos(Math.toRadians(90 - sweepAngle.toDouble())).toFloat()
        val y = centerY - radius * sin(Math.toRadians(90 - sweepAngle.toDouble())).toFloat()
        drawCircularPoints(centerX, centerY, radius, totalMinutes, canvas)
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


            canvas.drawCircle(x, y, 50f, indicatorPaint)

            canvas.drawText("$currentHour", centerX, centerY + (radius * 0.2f), digitalTextPaint)

            canvas.drawText(am_pm, centerX + (radius * cos(Math.toRadians(70.0)).toFloat()), centerY - (radius * 0.25.toFloat()), textPaint)
            canvas.drawText(totalMinutes.toString(), x, y + 10, textPaint)
            canvas.drawText("${temperature}°C", centerX + radius * 0.1f, centerY + radius * 0.4f, textPaint)
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


    private fun startAnimation(minutes: Int) {
        val initialsweepAngle = (minutes * 360 / 60f)
        clockAnimator = ValueAnimator.ofFloat(initialsweepAngle, 360f).apply {
            duration = TimeUnit.MINUTES.toMillis(60 - (minutes.toLong()))
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                    invalidate()
                }

                override fun onAnimationEnd(animation: Animator) {
                    sweepAngle = 0f
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {
                }


            })
            addUpdateListener {

                val gradientPositions = floatArrayOf(0f, 0.25f, 0.5f, 1f)
                val sweepGradient = setSweepAnim(colorsArray, gradientPositions)

                circlePaint.shader = sweepGradient

                val value = (it.animatedValue as Float)
                drawProgress(value)
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
        }

        clockAnimator.start()
    }

    private fun setSweepAnim(colors: IntArray, positions: FloatArray): SweepGradient {
        val sweepGradient = SweepGradient(
            width.toFloat() / 2, height.toFloat() / 2,
            colors, positions
        )
        val matrix = Matrix()
        matrix.setRotate(-90f, width.toFloat() / 2, height.toFloat() / 2)
        sweepGradient.setLocalMatrix(matrix)
        return sweepGradient
    }

    private fun drawCircularPoints(centerX: Float, centerY: Float, radius: Float, minutes: Int, canvas: Canvas) {
        val currentMinuteProgress = (minutes / 60.0f) * 360.0f
        val startPointAngle = 360 - currentMinuteProgress

        for (minute in 0 until 60) {
            val angle = startPointAngle - (minute * 6)
            val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY - radius * sin(Math.toRadians(angle.toDouble())).toFloat()

            canvas.drawCircle(x, y, 5f, pointPaint)
        }
    }


    private fun observeDataChanges() {
        clockVm.observeMinutesValue().observeForever { value ->
            totalMinutes = value
            invalidate()
            startAnimation(value)
        }
        clockVm.observeHoursValue().observeForever {
            currentHour = it.first
            am_pm = it.second
            invalidate()
        }


    }

    val weatherData = MutableLiveData<WeatherData>()


}

