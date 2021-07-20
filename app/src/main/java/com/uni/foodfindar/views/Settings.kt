package com.uni.foodfindar.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.uni.foodfindar.R


class Settings : AppCompatActivity() {
    lateinit var setting: Button
    lateinit var help: Button


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting = findViewById(R.id.settings)
        help = findViewById(R.id.help)


        setting.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }


        help.setOnClickListener{
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }



    }





    }


