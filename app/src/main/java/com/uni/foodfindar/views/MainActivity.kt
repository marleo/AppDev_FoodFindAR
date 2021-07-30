package com.uni.foodfindar.views

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.uni.foodfindar.ApiResponse
import com.uni.foodfindar.Places
import com.uni.foodfindar.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {


    private lateinit var cont: Context
    private lateinit var setting: Button
    private lateinit var nearby: Button
    private lateinit var filter: Button
    private lateinit var slider: Slider
    private lateinit var appKey: String
    private lateinit var logo: ImageView

    private var coordinates: String = "(46.616062,14.265438,46.626062,14.275438)" //Bounding Box - Left top(lat-), Left bottom(lon-), right top(lat+), right bottom(lon+)

    private var restaurantFilter = true
    private var cafeFilter = false
    private var barFilter = false

    private var checkedFilterArray = booleanArrayOf(restaurantFilter, cafeFilter, barFilter)

    private var logoClicked = 0


    private var bbSize = 0.0175
    private var userPosLon: Double? = 0.0
    private var userPosLat: Double? = 0.0
    private var bbLonMin: Double? = 0.0
    private var bbLatMin: Double? = 0.0
    private var bbLonMax: Double? = 0.0
    private var bbLatMax: Double? = 0.0

    private lateinit var pref: SharedPreferences
    private lateinit var placesList: ArrayList<Places>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cont = this

        setting = findViewById(R.id.settings)
        nearby = findViewById(R.id.nearby_button)
        filter = findViewById(R.id.filter)
        slider = findViewById(R.id.distance_slider)
        logo = findViewById(R.id.logo)
        appKey = resources.getString(R.string.preferences_key)

        createPreferences()


        setting.setOnClickListener {

            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        disableUI()

        logo.setOnClickListener{
                logoClicked++

            if(logoClicked == 5){
                logoClicked = 0
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")
                startActivity(i)
            }

        }


        filter.setOnClickListener {

            val builder = AlertDialog.Builder(cont, R.style.MyCheckBox)
            val filterArray = arrayOf("Restaurant", "Cafe", "Bar")
            builder.setTitle("Select your preferences!")
            builder.setMultiChoiceItems(filterArray, checkedFilterArray) { _: DialogInterface, which: Int, isChecked: Boolean ->
                checkedFilterArray[which] = isChecked
            }

            builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                showSnackbar("Selection confirmed", Snackbar.LENGTH_SHORT)
                val edit = pref.edit()

                edit.putBoolean("restaurant", checkedFilterArray[0])
                edit.putBoolean("cafe", checkedFilterArray[1])
                edit.putBoolean("bar", checkedFilterArray[2])

                edit.apply()

                restaurantFilter = checkedFilterArray[0]
                cafeFilter = checkedFilterArray[1]
                barFilter = checkedFilterArray[2]
            }
            builder.setNeutralButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            val dialog = builder.create()

            dialog.show()
        }

        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
            }

        })
        slider.addOnChangeListener { _, _, _ ->

            val edit = pref.edit()

            when (slider.value) {
                1.0f -> {
                    edit.putBoolean("sliderPos1", true)
                    edit.putBoolean("sliderPos2", false)
                    edit.putBoolean("sliderPos3", false)
                    edit.putBoolean("sliderPos4", false)

                    edit.apply()
                }
                2.0f -> {
                    edit.putBoolean("sliderPos1", false)
                    edit.putBoolean("sliderPos2", true)
                    edit.putBoolean("sliderPos3", false)
                    edit.putBoolean("sliderPos4", false)

                    edit.apply()
                }
                3.0f -> {
                    edit.putBoolean("sliderPos1", false)
                    edit.putBoolean("sliderPos2", false)
                    edit.putBoolean("sliderPos3", true)
                    edit.putBoolean("sliderPos4", false)

                    edit.apply()
                }
                4.0f -> {
                    edit.putBoolean("sliderPos1", false)
                    edit.putBoolean("sliderPos2", false)
                    edit.putBoolean("sliderPos3", false)
                    edit.putBoolean("sliderPos4", true)

                    edit.apply()
                }
            }
        }
        getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastKnownPosition()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), 1)
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownPosition() {
        val task: Task<Location> = LocationServices.getFusedLocationProviderClient(this).lastLocation
        task.addOnSuccessListener { location: Location? ->
            setBoundingBox(location)

            Log.i("Coordinates", coordinates)

            tryOverpasser()
        }
        task.addOnFailureListener { e: Exception ->
            e.printStackTrace()
        }
    }

    private fun tryOverpasser() = runBlocking {
        val job = launch {
            val queue = Volley.newRequestQueue(cont)
            val url = "http://overpass-api.de/api/interpreter?data=[out:json];(node[\"amenity\"~\"restaurant|bar|cafe\"]$coordinates;way[\"amenity\"~\"restaurant|bar|cafe\"]$coordinates;relation[\"amenity\"~\"restaurant|bar|cafe\"]$coordinates;);out body;>;out skel;" //building the link for requests

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { res ->
                        placesList = ArrayList<Places>()

                        val responseObject = Gson().fromJson(
                                res.toString(),
                                ApiResponse::class.java
                        )

                        for (element in responseObject.elements!!) {
                            val tag = element.tags
                            if (tag?.name != "" && tag?.name != null && tag.amenity == "restaurant" || tag?.amenity == "cafe" || tag?.amenity == "bar") {
                                if (element.lat != null && element.lon != null) {
                                    if (tag.housenumber != null) {
                                        placesList.add(
                                                Places(
                                                        tag.name,
                                                        "" + tag.street + " " + tag.housenumber,
                                                        tag.amenity,
                                                        element.lat,
                                                        element.lon,
                                                        tag.website,
                                                        distance(userPosLat!!, userPosLon!!, element.lat, element.lon)
                                                )
                                        )
                                    } else {
                                        placesList.add(
                                                Places(
                                                        tag.name,
                                                        tag.street,
                                                        tag.amenity,
                                                        element.lat,
                                                        element.lon,
                                                        tag.website,
                                                        distance(userPosLat!!, userPosLon!!, element.lat, element.lon)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                        placesList.sortBy { it.distance }

                        enableUI()

                        nearby.setOnClickListener {

                            val intent = Intent(cont, Nearby_locations::class.java)
                            val bundle = Bundle()
                            bundle.putParcelableArrayList("placesList", placesList)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        debugPlaces() //must be deleted later
                    },
                    { e ->
                        showSnackbar("Error. Check Internet/Location settings and restart!", -2)
                        e.printStackTrace()
                    }
            )
            queue.add(jsonObjectRequest)

            jsonObjectRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 50000
                }

                override fun getCurrentRetryCount(): Int {
                    return 50000
                }

                @Throws(VolleyError::class)
                override fun retry(error: VolleyError) {
                }
            }
        }
        job.join()
    }

    private fun enableUI() {
        showSnackbar("Locations fetched successfully!", Snackbar.LENGTH_SHORT)
        nearby.isEnabled = true
        filter.isEnabled = true
        slider.isEnabled = true
        slider.alpha = 1F
        filter.alpha = 1F
        nearby.alpha = 1F
    }

    private fun disableUI() {
        showSnackbar("Fetching Locations...", Snackbar.LENGTH_INDEFINITE)
        slider.isEnabled = false
        filter.isEnabled = false
        nearby.isEnabled = false
        slider.alpha = .5F
        nearby.alpha = .5F
        filter.alpha = .5F
    }

    private fun setBoundingBox(location: Location?) {
        userPosLat = location?.latitude
        userPosLon = location?.longitude
        bbLatMin = userPosLat?.minus(bbSize)
        bbLonMin = userPosLon?.minus(bbSize)
        bbLatMax = userPosLat?.plus(bbSize)
        bbLonMax = userPosLon?.plus(bbSize)

        coordinates = "($bbLatMin,$bbLonMin,$bbLatMax,$bbLonMax)"
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val p = 0.017453292519943295    // Math.PI / 180
        val a = 0.5 - cos((lat2 - lat1) * p) / 2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos((lon2 - lon1) * p)) / 2

        return 12742 * asin(sqrt(a)) // 2 * R; R = 6371 km
    }

    private fun createPreferences() {
        pref = getSharedPreferences(appKey, 0)
        val editPref = pref.edit()

        editPref.putBoolean("restaurant", true)
        editPref.putBoolean("cafe", false)
        editPref.putBoolean("bar", false)

        editPref.putBoolean("sliderPos1", false)
        editPref.putBoolean("sliderPos2", false)
        editPref.putBoolean("sliderPos3", true)
        editPref.putBoolean("sliderPos4", false)

        editPref.apply()
    }

    private fun showSnackbar(message: String, length: Int){
        val snack = Snackbar.make(findViewById(android.R.id.content), message, length)
        val view = snack.view
        val snackTextView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTextView.gravity = Gravity.CENTER_HORIZONTAL
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            snackTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        view.setBackgroundColor(1571)
        snack.show()
    }

    private fun debugPlaces() {
        Log.i("Place", "Debugging Places..")
        for (places in placesList) {
            Log.i(
                    "Place",
                    "${places.name} || ${places.address} || ${places.amenity} || Lat: ${places.lat} || Lon: ${places.lon} || ${places.website} || ${places.distance}km"
            )
        }
    } //Must be deleted later
}


