package com.uni.foodfindar.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.uni.foodfindar.R
import kotlinx.android.synthetic.main.activity_help.*

class Help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val githubButton: Button = findViewById<Button>(R.id.githubButton)
        val url: String = "https://github.com/marleo/AppDev_FoodFindAR/wiki"

        githubButton.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            ContextCompat.startActivity(this, i, null)
        }

        backButton.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }


    }


}