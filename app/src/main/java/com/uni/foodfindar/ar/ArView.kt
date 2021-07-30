package com.uni.foodfindar.ar

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.gms.location.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.ar.sceneform.AnchorNode
import com.uni.foodfindar.Places
import com.uni.foodfindar.R
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

class ArView: AppCompatActivity(), SensorEventListener {
    private val TAG = "ArView"

    private lateinit var arFragment: PlacesArFragment
    private lateinit var mapFragment: SupportMapFragment

    // Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Sensor
    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var anchorNode: AnchorNode? = null
    private var anchorNodeArray: MutableList<AnchorNode> = mutableListOf<AnchorNode>()
    private var place: Place? = null
    private var currentLocation: Location? = null
    private var latLonText : TextView? = null
    private var distance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isSupportedDevice()) {
            return
        }
        setContentView(R.layout.activity_arview)

        val bundle = intent.extras
        val destination = bundle?.getParcelable<Places>("Place")

        showSnackbar("Calibrate the device, then tap on the AR-Plane", Snackbar.LENGTH_INDEFINITE)

        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as PlacesArFragment
        latLonText = findViewById(R.id.latLonText)
        Log.i("PlaceName", destination?.name!!)
        place = Place(
                "0",
                destination.name!!,
                destination.distance!!.toString(),
                Geometry(GeometryLocation(destination.lat!!, destination.lon!!))
        )
        sensorManager = getSystemService()!!

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setUpAr()
        setUpMaps()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun showSnackbar(message: String, length: Int){
        val snack = Snackbar.make(findViewById(android.R.id.content), message, length).setAction("OK") {
            it.invalidate()
        }
        val view = snack.view
        val snackTextView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTextView.gravity = Gravity.CENTER_HORIZONTAL
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            snackTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        view.setBackgroundColor(1571)
        snack.show()
    }

    private fun setUpAr() {
        arFragment.setOnTapArPlaneListener { hit, _, _ ->
            // Create anchor
            Log.i("MainActivity", "Plain Tapped")

            val anchor = hit.createAnchor()
            anchorNode = AnchorNode(anchor)
            anchorNode?.setParent(arFragment.arSceneView.scene)
            addPlaces(anchorNode!!)
        }
    }

    private fun addPlaces(anchorNode: AnchorNode) {

        if(anchorNodeArray.isNotEmpty()){
            for(node in anchorNodeArray){
                arFragment.arSceneView.scene.removeChild(node)
                node.anchor?.detach()
                node.setParent(null)
                node.renderable = null
            }
        }

        anchorNodeArray.add(anchorNode)

        val currentLocation = currentLocation
        if (currentLocation == null) {
            Log.w(TAG, "Location has not been determined yet")
            return
        }

        if (place == null) {
            Log.w(TAG, "No places to put")
            return
        }

        // Add the place in AR
        val placeNode = PlaceNode(this, place)
        placeNode.setParent(anchorNode)
        placeNode.localPosition = place?.getPositionVector(
                orientationAngles[0],
                currentLocation.latLng
        )

        Toast.makeText(this, "Destination added, look around!", Toast.LENGTH_SHORT).show()

        Log.i("MainActivity", "Added place")
    }



    private fun setUpMaps() {
       getCurrentLocation {
            distance = calculateDistance(it)
            place?.distance = "${(distance*1000).toInt()}m"
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            onSuccess(location)
            latLonText?.text = "${location.longitude}, ${location.latitude}"
        }.addOnFailureListener {
            Log.e(TAG, "Could not get location")
        }
    }

    private fun calculateDistance(location: Location): Double {
        val lat2 = place!!.geometry.location.lat
        val lon2 = place!!.geometry.location.lng
        val lat1 = location.latitude
        val lon1 = location.longitude

        Log.i("MainActivity", "Lat1: $lat1 Lat2: $lat2 Lon1: $lon1 Lon2: $lon2 ")

        val p = 0.017453292519943295    // Math.PI / 180
        val a = 0.5 - cos((lat2 - lat1) * p) / 2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos((lon2 - lon1) * p)) / 2

        return 12742 * asin(sqrt(a)) // 2 * R; R = 6371 km
    }


    private fun isSupportedDevice(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val openGlVersionString = activityManager.deviceConfigurationInfo.glEsVersion
        if (openGlVersionString.toDouble() < 3.0) {
            Toast.makeText(this, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
            finish()
            return false
        }
        return true
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                accelerometerReading,
                magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }
}

val Location.latLng: LatLng
    get() = LatLng(this.latitude, this.longitude)