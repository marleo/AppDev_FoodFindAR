package com.uni.foodfindar.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.uni.foodfindar.R


class Settings : AppCompatActivity() {
    lateinit var setting: Button
    lateinit var help: Button
    lateinit var filter: Button
    lateinit var slider: Slider


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting = findViewById(R.id.settings)
        help = findViewById(R.id.help)
        filter = findViewById(R.id.nearby_button3)
        slider = findViewById(R.id.distance_slider2)
        slider.isEnabled = false
        filter.isEnabled = false
        filter.alpha = .5F


        setting.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }


        help.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }


    }


}


