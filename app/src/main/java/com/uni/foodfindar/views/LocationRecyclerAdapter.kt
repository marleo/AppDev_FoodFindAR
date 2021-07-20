package com.uni.foodfindar.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.uni.foodfindar.Places
import com.uni.foodfindar.R

class LocationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Places> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LocationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nearby_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LocationViewHolder ->{
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(locationList: List<Places>){
        items = locationList
    }

    class LocationViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationDistance: TextView = itemView.findViewById(R.id.location_distance)
        private var location_distance: Double = 1000.0
        private var locationWebsite: ImageView = itemView.findViewById(R.id.website)

        fun bind(location: Places){
            locationName.text = location.name
            location_distance *= location.distance!!
            locationDistance.text = location_distance.toInt().toString() + " m"
            if (location.website != null){
                locationWebsite.visibility = View.VISIBLE
                }

            }
        }
    }

