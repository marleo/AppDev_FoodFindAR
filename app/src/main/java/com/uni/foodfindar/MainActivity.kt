package com.uni.foodfindar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.westnordost.osmapi.OsmConnection
import de.westnordost.osmapi.overpass.OverpassMapDataApi
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tryOverpasser()
    }

    private fun tryOverpasser() {
        thread {
            val queue = Volley.newRequestQueue(this)
            val url = "http://overpass-api.de/api/interpreter?data=[out:json];(node[\"amenity\"~\"restaurant\"](46.616062,14.265438,46.626062,14.275438);way[\"amenity\"~\"restaurant\"](46.616062,14.265438,46.626062,14.275438);relation[\"amenity\"~\"restaurant\"](46.616062,14.265438,46.626062,14.275438););out body;>;out skel;"
            var res = ""

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    res = "Response: %s".format(response.toString())
                    Log.i("Response", "Im here")
                },
                { error ->
                    // TODO: Handle error
                }
            )

            queue.add(jsonObjectRequest)


// Add the request to the RequestQueue.

            /*
            val gson = Gson()

            try {
                val connection = OsmConnection("https://overpass-api.de/api/", "my user agent", null)
                val overpass = OverpassMapDataApi(connection)
                val count = overpass.quer
                Log.i("Query", gson.toJson(count))
            } catch (e : Exception) {
                Log.i("Error", "Error catch")
                e.printStackTrace()
            }
            */
        }
        Log.i("Response", "Response")
    }
}