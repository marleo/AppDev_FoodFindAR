package com.uni.foodfindar

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class Settings : AppCompatActivity() {
    lateinit var setting: Button
    lateinit var location: Button
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting = findViewById(R.id.settings)
        location = findViewById(R.id.standort)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setting.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<Button>(R.id.standort).setOnClickListener(){
            fetchLocation()
        }

    }
   private fun fetchLocation(){
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }

        task.addOnSuccessListener {
            if(it != null){
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                Gps().lati = it.latitude
                Gps().longi = it.longitude
                Toast.makeText(applicationContext, "${Gps().lati} ${Gps().longi}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}