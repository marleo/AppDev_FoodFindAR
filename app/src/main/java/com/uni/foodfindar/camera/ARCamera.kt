package com.uni.foodfindar.camera

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.opengl.Matrix
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.uni.foodfindar.R


class ARCamera: AppCompatActivity(), SensorEventListener, LocationListener {
    private val PERMISSION_CODE: Int = 1000
    private val directionToGo: Int = -1
    //WENN NACH LINKS DANN 0, GERADE AUS 1, 0 ANFANG, 4 ZIEL ... BZW. SÜDEN NORDEN.

    val TAG: String? = "ARActivity"
    private var surfaceView: SurfaceView? = null
    private val cameraContainerLayout: FrameLayout? = null
    private val arOverlayView: ViewCamera? = null
    private var ARCamera: ARCamera? = null
    private val arARCamera: ARCamera? = null
    private val tvCurrentLocation: TextView? = null
    private val tvBearing: TextView? = null


    var surfaceHolder: SurfaceHolder? = null
    var activity: Activity? = null

    var projectionMatrix = FloatArray(16)

    var cameraWidth = 0
    var cameraHeight = 0
    private val Z_NEAR = 0.5f
    private val Z_FAR = 10000f

    fun Camera(context: Context?, surfaceView: SurfaceView?) {
        this.surfaceView = surfaceView
        activity = context as Activity?
        surfaceHolder = this.surfaceView!!.holder
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                //Permission was not given
                val permission = arrayOf(Manifest.permission.CAMERA)
                //Show pop up to ask for permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //Permission was granted
                openCamera()
            }
        } else {
            //System is younger than marshmallow
            openCamera()
        }

    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "YOUR WAY TO GO")
        changeDirection()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    openCamera()
                } else {
                    //Permission was denied
                    Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    //WHAT KIND OF IMAGES SHOULD BE USED?
    private fun changeDirection(){
        //TODO change imageView to go to the correct location
        val direction = findViewById<ImageView>(R.id.direction)

        if(directionToGo == 0){
            //direction.setImageResource(R.id.new_image);
        } else if(directionToGo == 1){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 2){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 3){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 4){
            //direction.setImageResource(R.id.new_image);
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

    private fun getCameraOrientation(): Int {
        val info = android.hardware.Camera.CameraInfo()
        val rotation = activity!!.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var orientation: Int
        //if (info.facing == Camera.CAMERA_FACING_FRONT) { NOT WORKING
            orientation = (info.orientation + degrees) % 360
            orientation = (360 - orientation) % 360
        //} else {
            orientation = (info.orientation - degrees + 360) % 360
        //}
        return orientation
    }


    fun surfaceDestroyed(holder: SurfaceHolder?) {
        if (ARCamera != null) {
            ARCamera = null
        }
    }

    private fun generateProjectionMatrix() {
        var ratio = 0f
        ratio = if (cameraWidth < cameraHeight) {
            cameraWidth.toFloat() / cameraHeight
        } else {
            cameraHeight.toFloat() / cameraWidth
        }
        val OFFSET = 0
        val LEFT = -ratio
        val RIGHT = ratio
        val BOTTOM = -1f
        val TOP = 1f
        Matrix.frustumM(projectionMatrix, OFFSET, LEFT, RIGHT, BOTTOM, TOP, Z_NEAR, Z_FAR)
    }

    @JvmName("getProjectionMatrix1")
    fun getProjectionMatrix(): FloatArray? {
        return projectionMatrix
    }


}