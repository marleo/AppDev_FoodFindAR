package com.uni.foodfindar.camera

import android.content.Context
import android.graphics.*
import android.location.Location
import android.view.View


class ViewCamera : View(null) {
/*
    private var context: Context? = null
    private var rotatedProjectionMatrix = FloatArray(16)
    private var currentLocation: Location? = null
    private var arPointCameras: List<PointCamera>? = null


    fun ViewCamera(context: Context?) {
        //super(context)
        this.context = context

        //Demo points
        arPointCameras = object : ArrayList<PointCamera?>() { //PointCamera?
            init {
                add(PointCamera("Sun Wheel", 16.0404856, 108.2262447, 0))
                add(PointCamera("Linh Ung Pagoda", 16.1072989, 108.2343984, 0))
            }
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
        for (i in arPointCameras!!.indices) {

            //TODO GET LOCATION X AND Y
            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
                val x: Float = 0f
                val y: Float =
                    (0.5f - 0)
                canvas.drawCircle(x, y, radius.toFloat(), paint)
            arPointCameras!![i].getName()?.let {
                canvas.drawText(
                    it,
                    x - 30 * arPointCameras!![i].getName()?.length!! / 2,
                    y - 80,
                    paint
                )
            }
        }
    }

 */
}