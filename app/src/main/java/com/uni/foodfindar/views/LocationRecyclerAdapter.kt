package com.uni.foodfindar.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.uni.foodfindar.Places
import com.uni.foodfindar.R



class LocationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Places> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LocationViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.nearby_list_item,
                    parent,
                    false
                ), context
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is LocationViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(locationList: List<Places>, context: Context){
        items = locationList
        this.context = context
    }

    class LocationViewHolder constructor(itemView: View, context: Context): RecyclerView.ViewHolder(
        itemView
    ) {

        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationDistance: TextView = itemView.findViewById(R.id.location_distance)
        private var locationDistanceString: String = ""
        private var locationWebsite: ImageView = itemView.findViewById(R.id.website)
        private var cont: Context = context

        fun bind(location: Places){
            locationName.text = location.name
            locationDistanceString = (location.distance?.times(1000)?.toInt()).toString() + "m"
            locationDistance.text = locationDistanceString
           //viewCamera.addLocation(location)

            if (location.website != null){
                locationWebsite.visibility = View.VISIBLE
            }

            locationWebsite.setOnClickListener{
                val url = location.website
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                Toast.makeText(cont, "${this.locationName.text} clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(cont, com.uni.foodfindar.arview.ArView :: class.java)
                val bundle = Bundle()
                bundle.putParcelable("Location", location)
                intent.putExtras(bundle)
                startActivity(cont, intent, null)

            }
        }
    }
}

