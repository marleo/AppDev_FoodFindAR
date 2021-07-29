package com.uni.foodfindar.arview.api


import com.google.gson.annotations.SerializedName
import com.uni.foodfindar.arview.Place

class NearbyPlacesResponse {
    @SerializedName("results") var results: List<Place> = emptyList()

}