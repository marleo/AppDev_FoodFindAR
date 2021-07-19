package com.uni.foodfindar

class Places(
    val name : String,
    val address : String?,
    val lat : Double?,
    val lon : Double?,
    val website : String?,
    val distance : Double? = 0.0
)