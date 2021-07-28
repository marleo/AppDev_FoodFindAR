package com.uni.foodfindar.camera

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.*
import android.opengl.Matrix
import android.util.Log
import android.view.*
import java.io.IOException
import kotlin.math.abs


class ARCamera(context: Context?, val surfaceView: SurfaceView?) : SurfaceHolder.Callback,
    ViewGroup(context) {

    private val TAG = "ARCamera"

    private var surfaceHolder: SurfaceHolder? = null
    private var previewSize: Camera.Size? = null
    private var supportedPreviewSizes: List<Camera.Size>? = null
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
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


    fun getOptimalPreviewSize(sizes: List<Camera.Size>, width: Int, height: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = width.toDouble() / height
        if (sizes == null) return null

        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE

        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue
            }
            if (abs(size.height - height) < minDiff) {
                optimalSize = size
                minDiff = abs(size.height - height).toDouble()
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (abs(size.height - height) < minDiff) {
                    optimalSize = size
                    minDiff = abs(size.height - height).toDouble()
                }
            }
        }

        if (optimalSize == null) {
            optimalSize = sizes[0];
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


}