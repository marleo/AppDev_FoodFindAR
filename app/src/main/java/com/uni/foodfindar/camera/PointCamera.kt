package com.uni.foodfindar.camera

import android.location.Location

class PointCamera(s: String, d: Double, d1: Double, i: Int) {
    private var location: Location? = null
    private var name: String? = null

    fun PointCamera(name: String?, lat: Double, lon: Double, altitude: Double) {
        this.name = name
        location = Location("Point")
        location!!.latitude = lat
        location!!.longitude = lon
        location!!.altitude = altitude
    }

    fun getLocation(): Location? {
        return location
    }

    fun getName(): String? {
        return name
    }
}