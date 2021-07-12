package com.uni.foodfindar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.uni.foodfindar.CameraView.CameraMain

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