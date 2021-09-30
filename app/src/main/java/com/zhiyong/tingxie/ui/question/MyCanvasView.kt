package com.zhiyong.tingxie.ui.question

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

private const val STROKE_WIDTH = 12f

class MyCanvasView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
): View(context, attrs) {
  private lateinit var extraCanvas: Canvas
  lateinit var extraBitmap: Bitmap
  // Set up the paint with which to draw.
  private val paint = Paint().apply {
    // Smooths out edges of what is drawn without affecting shape.
    isAntiAlias = true
    // Dithering affects how colors with higher-precision than the device are down-sampled.
    isDither = true
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
    strokeWidth = STROKE_WIDTH
  }
  private var path = Path()
  private var motionTouchEventX = 0f
  private var motionTouchEventY = 0f
  private var currentX = 0f
  private var currentY = 0f
  private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

  override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
    super.onSizeChanged(width, height, oldWidth, oldHeight)
    if (::extraBitmap.isInitialized) extraBitmap.recycle()
    extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    extraCanvas = Canvas(extraBitmap)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    // Draw the bitmap that has the saved path.
    canvas.drawBitmap(extraBitmap, 0f, 0f, null)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    motionTouchEventX = event.x
    motionTouchEventY = event.y

    when(event.action) {
      MotionEvent.ACTION_DOWN -> touchStart()
      MotionEvent.ACTION_MOVE -> touchMove()
      MotionEvent.ACTION_UP -> touchUp()
    }

    return true
  }

  private fun touchStart() {
    path.reset()
    path.moveTo(motionTouchEventX, motionTouchEventY)
    currentX = motionTouchEventX
    currentY = motionTouchEventY
  }

  private fun touchMove() {
    val dx = abs(motionTouchEventX - currentX)
    val dy = abs(motionTouchEventY - currentY)
    if (dx >= touchTolerance || dy >= touchTolerance) {
      // QuadTo() adds a quadratic bezier from the last point.
      // approaching control point (x1,y1), and ending at (x2,y2).
      path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
      currentX = motionTouchEventX
      currentY = motionTouchEventY
      // Draw the path in the extra bitmap to cache it.
      extraCanvas.drawPath(path, paint)
    }
    invalidate()
  }

  private fun touchUp() {
    // Reset the path so it doesn't get drawn again.
    path.reset()
  }
}