package com.uni.foodfindar.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.location.Location
import android.opengl.Matrix
import android.util.Log
import android.view.View
import com.uni.foodfindar.Places


class ViewCamera(context: Context?, place: Places?) : View(context) {

    private var rotatedProjectionMatrix = FloatArray(16)
    private var currentLocation: Location? = null
    private var arPoints: MutableList<PointCamera> = mutableListOf()

    init{
        if (place != null) {
            arPoints.add(PointCamera(place.name!!, place.lat!!, place.lon!!, 1050.0))
        }
    }


    fun updateRotatedProjectionMatrix(rotatedProjectionMatrix: FloatArray?) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix!!
        this.invalidate()
    }

    fun updateCurrentLocation(currentLocation: Location?) {
        this.currentLocation = currentLocation
        this.invalidate()
    }


    @SuppressLint("DrawAllocation")
    public override fun onDraw(canvas: Canvas) {
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

        Log.i("HALLODVJ", arPoints.size.toString())
       for (i in arPoints.indices) {
           // Log.i("hsdbf", i.toString())
            val currentLocationInECEF: FloatArray? =                  //Earth-Centered, Earth-Fixed
                LocationHelper.WSG84toECEF(currentLocation!!)
            val pointInLongitude: FloatArray =
                LocationHelper.WSG84toECEF(arPoints[i].getLocation()!!)!!
            val pointInENU: FloatArray =                                           // East-North-Up
                LocationHelper.ECEFtoENU(currentLocation!!, currentLocationInECEF!!, pointInLongitude)!!

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