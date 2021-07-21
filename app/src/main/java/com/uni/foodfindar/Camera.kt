package com.uni.foodfindar

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Camera: AppCompatActivity() {
    private val PERMISSION_CODE: Int = 1000
    private val directionToGo: Int = -1
    //WENN NACH LINKS DANN 0, GERADE AUS 1, 0 ANFANG, 4 ZIEL ... BZW. SÜDEN NORDEN.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                //Permission was not given
                val permission = arrayOf(Manifest.permission.CAMERA)
                //Show pop up to ask for permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //Permission was granted
                openCamera()
            }
        } else {
            //System is younger than marshmallow
            openCamera()
        }

    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "YOUR WAY TO GO")
        changeDirection()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    openCamera()
                } else {
                    //Permission was denied
                    Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    //WHAT KIND OF IMAGES SHOULD BE USED?
    private fun changeDirection(){
        //TODO change imageView to go to the correct location
        val direction = findViewById<ImageView>(R.id.direction)

        if(directionToGo == 0){
            //direction.setImageResource(R.id.new_image);
        } else if(directionToGo == 1){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 2){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 3){
            //direction.setImageResource(R.id.new_image);
        }else if(directionToGo == 4){
            //direction.setImageResource(R.id.new_image);
        }
    }
}