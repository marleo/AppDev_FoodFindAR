package com.uni.foodfindar.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.location.Location
import android.opengl.Matrix
import android.view.View


class ViewCamera(context: Context?) : View(context) {

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
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 60f

        for (i in arPoints!!.indices) {

            val currentLocationInECEF: FloatArray? = LocationHelper.WSG84toECEF(currentLocation!!)
            val pointInECEF: FloatArray =
                LocationHelper.WSG84toECEF(arPoints!![i].getLocation()!!)!!
            val pointInENU: FloatArray =
                LocationHelper.ECEFtoENU(currentLocation!!, currentLocationInECEF!!, pointInECEF)!!
            val cameraCoordinateVector = FloatArray(4)
            Matrix.multiplyMV(
                cameraCoordinateVector, 0, rotatedProjectionMatrix,
                0, pointInENU, 0
            )

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                val x =
                    (0.5f + cameraCoordinateVector[0] / cameraCoordinateVector[3]) * canvas.width
                val y =
                    (0.5f - cameraCoordinateVector[1] / cameraCoordinateVector[3]) * canvas.height
                canvas.drawCircle(x, y, radius.toFloat(), paint)
                canvas.drawText(
                    arPoints!![i].getName()!!,
                    x - 30 * arPoints!![i].getName()!!.length / 2,
                    y - 80,
                    paint
                )
            }
        }
    }
}