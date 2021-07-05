package com.uni.foodfindar

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val coordinates: String = "(46.616062,14.265438,46.626062,14.275438)" //Bounding Box - Left top(lat-), Left bottom(lon-), right top(lat+), right bottom(lon+)
    var amenity: String = "[\"amenity\"~\"restaurant\"]" //Amenity can be extended with e.g. restaurant|cafe etc.
    var longitude : Double? = 0.0
    var latitude : Double? = 0.0
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    @SuppressLint("MissingPermission") //TODO: CHECK FAILURE (val location)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tryOverpasser()


        //TODO: Check periodically, Ask user for location permission
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Log.i("Location", "Location tracked!")
            longitude = location?.longitude
            latitude = location?.latitude
            Log.i("Location", "Lat: $latitude Lon: $longitude ")
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e : Exception ->
            Log.i("Location", "Location failed!")
            e.printStackTrace()
        }



    /*
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        longitude = location?.longitude
        latitude = location?.latitude
     */

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
                            if (element.lat != 0.0 && element.lat != null &&  element.lon != 0.0 && element.lon != null && element.tags?.amenity == "restaurant") {
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