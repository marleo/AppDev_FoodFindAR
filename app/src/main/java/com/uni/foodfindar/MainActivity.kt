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

class MainActivity : AppCompatActivity() {

    lateinit var setting:Button
    lateinit var menu:Button
    lateinit var search:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setting = findViewById(R.id.settings)
        menu = findViewById(R.id.menu)
        search = findViewById(R.id.search)


        search.setOnClickListener{
            Toast.makeText(applicationContext, "${Gps().lati} ${Gps().longi}", Toast.LENGTH_SHORT).show()
        }


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


    }

}