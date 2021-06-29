package com.uni.foodfindar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val coordinates : String = "(46.616062,14.265438,46.626062,14.275438)" //Bounding Box - Left top(lat-), Left bottom(lon-), right top(lat+), right bottom(lon+)
    var amenity : String = "[\"amenity\"~\"restaurant\"]" //Amenity can be extended with e.g. restaurant|cafe etc.



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tryOverpasser()
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

                    for(element in responseObject.elements!!){
                        if(element.tags?.name != "" && element.tags?.name != null && element.tags.amenity == "restaurant"){
                            nameList.add(element.tags.name)
                        }

                        if(element.tags?.street != "" && element.tags?.street != null && element.tags.amenity == "restaurant"){
                            streetList.add(element.tags.street)
                        }
                    }

                    Log.i("Names", nameList.toString())
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