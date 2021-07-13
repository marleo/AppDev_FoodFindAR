package com.uni.foodfindar.CameraView

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CameraMain : AppCompatActivity(){

   val destination = 9.14;
   private val PERMISSION_CODE: Int = 1000


   override fun onCreate(savedInstanceState: Bundle?){
      super.onCreate(savedInstanceState)

      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
         if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            //Permission was not given
            val permission = arrayOf(Manifest.permission.CAMERA)
            //Show pop up to ask for permission
            requestPermissions(permission,PERMISSION_CODE)
         }else{
            //Permission was granted
            openCamera()
         }
      }else{
         //System is younger than marshmallow
         openCamera()
      }

   }

   private fun openCamera() {
      val values = ContentValues()
      values.put(MediaStore.Images.Media.TITLE, "YOUR WAY TO GO")
   }

   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
   ) {
      when(requestCode){
         PERMISSION_CODE -> {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               //Permission was granted
               openCamera()
            }else{
               //Permission was denied
               Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show()

            }
         }
      }
   }


}