package com.example.groupproject.finalproject

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import android.util.Log
import org.osmdroid.util.GeoPoint

class TextOverlay(private val mapView: MapView, private val geoPoint: GeoPoint, private val text: String) : Overlay() {

    private val paint: Paint = Paint()
    private val bounds: Rect = Rect()
    private val point: Point = Point()

    init {
        paint.color = Color.BLACK
        paint.textSize = 32f // Set text size as needed
        paint.textAlign = Paint.Align.CENTER
    }


    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (!shadow) {
            // Log the text value

            val projection = mapView.projection
            projection.toPixels(geoPoint, point)

            val x = point.x.toFloat()
            val y = point.y.toFloat() -20f

            canvas.drawText(text, x, y, paint)
        }
    }
}