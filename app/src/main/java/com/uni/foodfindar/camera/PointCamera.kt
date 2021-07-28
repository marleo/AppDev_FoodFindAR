package com.uni.foodfindar.camera

import android.location.Location

class PointCamera(s: String, d: Double, d1: Double, i: Double) {
    private var location: Location? = null
    private var name: String? = null

    init{
        this.name = s
        location = Location("Point")
        location!!.latitude =  d
        location!!.longitude = d1
        location!!.altitude = i
    }

    fun getLocation(): Location? {
        return location
    }

    fun getName(): String? {
        return name
    }
}