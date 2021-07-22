package com.uni.foodfindar.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.uni.foodfindar.R
import kotlinx.android.synthetic.main.activity_settings.*


class Settings : AppCompatActivity() {
    lateinit var setting: Button
    lateinit var help: Button


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting = findViewById(R.id.settings)
        help = findViewById(R.id.help)




        settingLayout.setOnTouchListener { _, _ ->
            finish()
            overridePendingTransition(0, 0)

            true
        }




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


