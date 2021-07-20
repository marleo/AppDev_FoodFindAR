package com.uni.foodfindar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var buttonCamera = findViewById<Button>(R.id.buttonCamera);

        buttonCamera.setOnClickListener {
            val intent = Intent(this, CameraMain::class.java)
            startActivity(intent);
        }
    }


}