package com.uni.foodfindar

import com.google.gson.annotations.SerializedName

data class ApiResponse(
        val version: Double? = null,
        val generator: String? = null,
        val osm3s: OSM3S? = null,
        val elements: List<Elements>? = null
)

data class OSM3S(
        val timestamp_osm_base: String? = null,
        val copyright: String? = null
)

data class Elements(
        val type: String? = null,
        val id: Long? = null,
        val lat: Double? = null,
        val lon: Double? = null,
        val tags: Tags? = null
)

data class Tags(
        @SerializedName("addr:city")
        val city: String? = null,
        @SerializedName("addr:country")
        val country: String? = null,
        @SerializedName("addr:housenumber")
        val housenumber: String? = null,
        @SerializedName("addr:postcode")
        val postcode: String? = null,
        @SerializedName("addr:street")
        val street: String? = null,
        val amenity: String? = null,
        val cuisine: String? = null,
        val name: String? = null,
        val opening_hours: String? = null,
        val phone: String? = null,
        val smoking: String? = null,
        val website: String? = null,
        val wheelchair: String? = null
)
