package com.uni.foodfindar.camera
import android.app.Activity
import android.content.Context
import android.hardware.*
import android.opengl.Matrix
import android.util.Log
import android.view.*
import java.io.IOException


class ARCamera(context: Context?, val surfaceView: SurfaceView?) : SurfaceHolder.Callback, ViewGroup(context) {

    private val TAG = "ARCamera"

    var surfaceHolder: SurfaceHolder? = null
    var previewSize: Camera.Size? = null
    var supportedPreviewSizes: List<Camera.Size>? = null
    var camera: Camera? = null
    var parameters: Camera.Parameters? = null
    var activity: Activity? = null

    var projectionMatrix = FloatArray(16)

    var cameraWidth = 0
    var cameraHeight = 0
    private val Z_NEAR = 0.5f
    private val Z_FAR = 10000f

    init {
        activity = context as Activity?
        surfaceHolder = this.surfaceView!!.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    @JvmName("setCamera1")
    fun setCamera(camera: Camera?) {
        this.camera = camera
        if (this.camera != null) {
            supportedPreviewSizes = this.camera!!.parameters.supportedPreviewSizes
            requestLayout()
            val params = this.camera!!.parameters
            val focusModes = params.supportedFocusModes
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                this.camera!!.parameters = params
            }
        }
    }


    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec)
        val height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec)
        setMeasuredDimension(width, height)
        if (supportedPreviewSizes != null) {
            previewSize = getOptimalPreviewSize(supportedPreviewSizes!!, width, height)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed && childCount > 0) {
            val child: View = getChildAt(0)
            val width = right - left
            val height = bottom - top
            var previewWidth = width
            var previewHeight = height
            if (previewSize != null) {
                previewWidth = previewSize!!.width
                previewHeight = previewSize!!.height
            }
            if (width * previewHeight > height * previewWidth) {
                val scaledChildWidth = previewWidth * height / previewHeight
                child.layout(
                    (width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height
                )
            } else {
                val scaledChildHeight = previewHeight * width / previewWidth
                child.layout(
                    0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2
                )
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            if (camera != null) {
                parameters = camera!!.parameters
                val orientation: Int = getCameraOrientation()
                camera!!.setDisplayOrientation(orientation)
                camera!!.parameters.setRotation(orientation)
                camera!!.setPreviewDisplay(holder)
            }
        } catch (exception: IOException) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception)
        }
    }

    private fun getCameraOrientation(): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info)

        val rotation = activity!!.windowManager.defaultDisplay.rotation

        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var orientation: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            orientation = (info.orientation + degrees) % 360
            orientation = (360 - orientation) % 360
        } else {
            orientation = (info.orientation - degrees + 360) % 360
        }

        return orientation
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (camera != null) {
            cameraWidth = width
            cameraHeight = height
            val params = camera!!.parameters
            params.setPreviewSize(previewSize!!.width, previewSize!!.height)
            requestLayout()
            camera!!.parameters = params
            camera!!.startPreview()
            generateProjectionMatrix()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (camera != null) {
            camera!!.setPreviewCallback(null);
            camera!!.stopPreview();
            camera!!.release();
            camera = null;
        }
    }


    fun getOptimalPreviewSize(sizes: List<Camera.Size> , width: Int , height: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = width.toDouble() / height
        if (sizes == null) return null

        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE

        val targetHeight = height

        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - targetHeight).toDouble()
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - targetHeight).toDouble()
                }
            }
        }

        if(optimalSize == null) {
            optimalSize = sizes.get(0);
        }

        return optimalSize
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

    /*
   val TAG = "ARActivity"
   private var surfaceView: SurfaceView? = null
   private var cameraContainerLayout: FrameLayout? = null
   private var arOverlayView: ViewCamera? = null
   private var camera: Camera? = null
   private var arCamera: ARCamera? = null
   private var tvCurrentLocation: TextView? = null
   private var tvBearing: TextView? = null

   private var sensorManager: SensorManager? = null
   private val REQUEST_CAMERA_PERMISSIONS_CODE = 11
   val REQUEST_LOCATION_PERMISSIONS_CODE = 0

   private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0 // 10 meters

   private val MIN_TIME_BW_UPDATES: Long = 0 //1000 * 60 * 1; // 1 minute


   private val locationManager: LocationManager? = null
   var location: Location? = null
   var isGPSEnabled = false
   var isNetworkEnabled = false
   var locationServiceAvailable = false
   private val declination = 0f


   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_camera)


       sensorManager = this.getSystemService(SENSOR_SERVICE) as SensorManager
       cameraContainerLayout = findViewById(R.id.camera_container_layout)
       surfaceView = findViewById(R.id.surface_view)
       tvCurrentLocation = findViewById(R.id.tv_current_location)
       tvBearing = findViewById(R.id.tv_bearing)
       arOverlayView = ViewCamera()

   }

   override fun onResume() {
       super.onResume()
       requestCameraPermission()
       requestLocationPermission()
       registerSensors()
       initAROverlayView()
   }

   override fun onPause() {
       releaseCamera()
       super.onPause()
   }

   fun requestCameraPermission() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
           checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
       ) {
           requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS_CODE)
       } else {
           initARCameraView()
       }
   }

   fun requestLocationPermission() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
           checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
       ) {
           requestPermissions(
               arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
               REQUEST_LOCATION_PERMISSIONS_CODE
           )
       } else {
           initLocationService()
       }
   }

   fun initAROverlayView() {
       if (arOverlayView!!.parent != null) {
           (arOverlayView!!.parent as ViewGroup).removeView(arOverlayView)
       }
       cameraContainerLayout!!.addView(arOverlayView)
   }

   fun initARCameraView() {
       reloadSurfaceView()
       if (arCamera == null) {
           arCamera = ARCamera(this, surfaceView)
       }
       if (arCamera.getParent() != null) {
           (arCamera.getParent() as ViewGroup).removeView(arCamera)
       }
       cameraContainerLayout!!.addView(arCamera)
       arCamera.setKeepScreenOn(true)
       initCamera()
   }

   private fun initCamera() {
       val numCams = Camera.getNumberOfCameras()
       if (numCams > 0) {
           try {
               camera = Camera.open()
               camera.startPreview()
               arCamera.setCamera(camera)
           } catch (ex: RuntimeException) {
               Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG).show()
           }
       }
   }

   private fun reloadSurfaceView() {
       if (surfaceView!!.parent != null) {
           (surfaceView!!.parent as ViewGroup).removeView(surfaceView)
       }
       cameraContainerLayout!!.addView(surfaceView)
   }

   private fun releaseCamera() {
       if (camera != null) {
           camera!!.setPreviewCallback(null)
           camera!!.stopPreview()
           arCamera.setCamera(null)
           camera!!.release()
           camera = null
       }
   }

   private fun registerSensors() {
       sensorManager!!.registerListener(
           this,
           sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
           SENSOR_DELAY_NORMAL
       )
   }



   //////////////////////////
   private fun openCamera() {
       val values = ContentValues()
       values.put(MediaStore.Images.Media.TITLE, "YOUR WAY TO GO")
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


   override fun onSensorChanged(event: SensorEvent?) {
       val rotationMatrixFromVector = FloatArray(16)
       val rotationMatrix = FloatArray(16)
       getRotationMatrixFromVector(rotationMatrixFromVector, sensorEvent.values)
       val screenRotation = this.windowManager.defaultDisplay
           .rotation

       when(screenRotation){
           ROTATION_90
       }

   }

   override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
       TODO("Not yet implemented")
   }

   override fun onLocationChanged(location: Location) {
       TODO("Not yet implemented")
   }

   /*
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

    */

    */

}