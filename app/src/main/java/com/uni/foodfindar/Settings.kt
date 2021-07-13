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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class Settings : AppCompatActivity() {
    lateinit var setting: Button
    lateinit var location: Button
    lateinit var filter: Button
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
        filter = findViewById(R.id.filter)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setting.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }

        filter.setOnClickListener{
            showDialog()
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
            }
        }

    }

    private fun showDialog(){
        lateinit var dialog:AlertDialog

        val arrayLocations = arrayOf("Cafe","Restaurant", "Bar")
        val arrayChecked = ArrayList<Int>()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose your Preferences!")

        builder.setMultiChoiceItems(arrayLocations, null) { dialog, which, isChecked ->

            if(isChecked){
                arrayChecked.add(which)
            } else if (arrayChecked.contains(which)){
                arrayChecked.remove(Integer.valueOf(which))
            }


        }

        builder.setPositiveButton("OK"){ _, _ ->


        }

        builder.setNegativeButton("Cancel"){_, _ ->
            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()

        }
    }


