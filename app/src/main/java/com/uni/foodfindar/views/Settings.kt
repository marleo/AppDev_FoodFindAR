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
    lateinit var location: Button
    lateinit var filter: Button
    lateinit var help: Button
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setting = findViewById(R.id.settings)
        filter = findViewById(R.id.filter)
        help = findViewById(R.id.help)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setting.setOnClickListener{
            finish()
            overridePendingTransition(0, 0)
        }

        filter.setOnClickListener{
            showDialog()
        }

        help.setOnClickListener{
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
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
            Toast.makeText(applicationContext, "Auswahl wurde übernommen!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel"){_, _ ->
            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()

        }
    }


