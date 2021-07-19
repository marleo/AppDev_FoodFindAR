package com.uni.foodfindar.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.uni.foodfindar.R

class Menu : AppCompatActivity() {
    lateinit var menu: Button

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)



        menu = findViewById(R.id.menu)

        menu.setOnClickListener{
            finish()
            overridePendingTransition(0,0)
        }
    }
}