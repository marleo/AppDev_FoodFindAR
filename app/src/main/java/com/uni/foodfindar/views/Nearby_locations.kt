package com.uni.foodfindar.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uni.foodfindar.Places
import com.uni.foodfindar.R
import java.io.Serializable

class Nearby_locations : AppCompatActivity() {
    private lateinit var location: RecyclerView
    private lateinit var locationAdapter: LocationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_locations)
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet(){
        val data = intent.getSerializableExtra("list") as ArrayList<Places>
        locationAdapter.submitList(data)
    }

    private fun initRecyclerView(){
        location = findViewById(R.id.nearby_list)

        location.apply {
            layoutManager = LinearLayoutManager(this@Nearby_locations)
            val topSpacingitemDecoration = TopSpacingitemDecoration(30)
            addItemDecoration(topSpacingitemDecoration)
            locationAdapter = LocationRecyclerAdapter()
            adapter = locationAdapter
        }
    }
}