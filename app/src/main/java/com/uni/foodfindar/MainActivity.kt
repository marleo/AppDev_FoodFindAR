package com.uni.foodfindar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var setting:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setting = findViewById(R.id.settings)

        setting.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }


    }
}