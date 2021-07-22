package com.uni.foodfindar.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uni.foodfindar.Places
import com.uni.foodfindar.R

class Nearby_locations : AppCompatActivity() {
    private lateinit var location: RecyclerView
    private lateinit var locationAdapter: LocationRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val list = (bundle?.getParcelableArrayList<Places>("placesList") as List<Places>)
        setContentView(R.layout.activity_nearby_locations)
        initRecyclerView()
        addDataSet(list)
    }

    private fun addDataSet(list: List<Places>) {
        locationAdapter.submitList(list, this)

    }

    private fun initRecyclerView() {
        location = findViewById(R.id.nearby_list)

        location.apply {
            layoutManager = LinearLayoutManager(this@Nearby_locations)
            locationAdapter = LocationRecyclerAdapter()
            adapter = locationAdapter
        }
    }

}