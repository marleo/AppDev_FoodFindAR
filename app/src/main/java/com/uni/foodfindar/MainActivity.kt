package com.uni.foodfindar

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import java.lang.Exception
import kotlin.concurrent.thread

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var setting:Button
    lateinit var menu:Button
    lateinit var search:Button




    val coordinates: String = "(46.616062,14.265438,46.626062,14.275438)" //Bounding Box - Left top(lat-), Left bottom(lon-), right top(lat+), right bottom(lon+)
    var amenity: String = "[\"amenity\"~\"restaurant\"]" //Amenity can be extended with e.g. restaurant|cafe etc.
    var longitude : Double? = 0.0
    var latitude : Double? = 0.0
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setting = findViewById(R.id.settings)
        menu = findViewById(R.id.menu)
        search = findViewById(R.id.search)



        setting.setOnClickListener{

            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        menu.setOnClickListener{
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }



        tryOverpasser()
        getLocation()

        val button : Button = findViewById(R.id.button)

        button.setOnClickListener {
            val intent = Intent(this, activity_map::class.java)
            intent.putExtra("Longitude", longitude)
            intent.putExtra("Latitude", latitude)
            startActivity(intent)
        }
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
        task.addOnSuccessListener { location : Location? ->
            Log.i("Sensors", "Time=${location?.time} Latitude=${location?.latitude} Longitude=${location?.longitude}")
            latitude = location?.latitude
            longitude = location?.longitude
        }
        task.addOnFailureListener { e : Exception ->
            e.printStackTrace()
        }
    }


        private fun tryOverpasser() {
            thread {
                val queue = Volley.newRequestQueue(this)
                val url = "http://overpass-api.de/api/interpreter?data=[out:json];(node$amenity$coordinates;way$amenity$coordinates;relation$amenity$coordinates;);out body;>;out skel;" //building the link for requests

                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                        { response ->
                            val responseObject = Gson().fromJson(response.toString(), ApiResponse::class.java)

                            val nameList = mutableListOf<String>()
                            val streetList = mutableListOf<String>()
                            val coordinateList = mutableListOf<Double>()

                            //TODO: Write to object
                            for (element in responseObject.elements!!) {
                                if (element.tags?.name != "" && element.tags?.name != null && element.tags.amenity == "restaurant") {
                                    nameList.add(element.tags.name)
                                }
                                if (element.tags?.street != "" && element.tags?.street != null && element.tags.amenity == "restaurant") {
                                    streetList.add(element.tags.street)
                                }
                                if (element.lat != 0.0 && element.lat != null && element.lon != 0.0 && element.lon != null && element.tags?.amenity == "restaurant") {
                                    coordinateList.add(element.lat)
                                    coordinateList.add(element.lon)
                                }
                            }

                            //TODO: Remove Logs
                            Log.i("Names", nameList.toString())
                            Log.i("Coordinates (Lat)", coordinateList.toString())
                            Log.i("Streets", streetList.toString())
                        },
                        { error ->
                            error.printStackTrace()
                        }
                )
                queue.add(jsonObjectRequest)
            }
            Log.i("Response", "Response")
        }

}
