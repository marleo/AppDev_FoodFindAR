package com.uni.foodfindar.camera

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.location.Location
import android.view.View


class ViewCamera() : View(null) {

    //private val context: Context? = context
    private var rotatedProjectionMatrix = FloatArray(16)
    private var currentLocation: Location? = null
    private var arPoints: List<PointCamera>? = null


    fun updateRotatedProjectionMatrix(rotatedProjectionMatrix: FloatArray?) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix!!
        this.invalidate()
    }

    fun updateCurrentLocation(currentLocation: Location?) {
        this.currentLocation = currentLocation
        this.invalidate()
    }


    //NEED THE DATA!!!!
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (currentLocation == null) {
            return
        }
        val radius = 30
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.WHITE)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        for (i in arPoints!!.indices) {

            //TODO GET LOCATION X AND Y
            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
                val x: Float = 0f
                val y: Float =
                    (0.5f - 0)
                canvas.drawCircle(x, y, radius.toFloat(), paint)
            arPoints!![i].getName()?.let {
                canvas.drawText(
                    it,
                    x - 30 * arPoints!![i].getName()?.length!! / 2,
                    y - 80,
                    paint
                )
            }
        }
    }


}