package com.uni.foodfindar.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import java.lang.IllegalArgumentException


class LocationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Places> = ArrayList()
    private var adapterDataList: List<Any> = emptyList()
    private lateinit var context: Context

    companion object{
        private const val TYPE_RESTAURANT = 0
        private const val TYPE_CAFE = 1
        private const val TYPE_BAR = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType){
                    TYPE_RESTAURANT -> {
                        val view = LayoutInflater.from(context)
                                .inflate(R.layout.nearby_list_item_restaurant, parent, false)
                        RestaurantViewHolder(view, context)
                    }
                    TYPE_CAFE -> {
                        val view = LayoutInflater.from(context)
                                .inflate(R.layout.nearby_list_item_cafe, parent, false)
                        CafeViewHolder(view, context)
                    }
                    TYPE_BAR -> {
                        val view = LayoutInflater.from(context)
                                .inflate(R.layout.nearby_list_item_bar, parent, false)
                        BarViewHolder(view, context)
                    }
                    else -> throw  IllegalArgumentException("Invalid view type")
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RestaurantViewHolder -> {
                holder.bind(items[position])
            }
            is CafeViewHolder -> {
                holder.bind(items[position])
            }
            is BarViewHolder -> {
                holder.bind(items[position])
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = items[position]

        return when (comparable.amenity){
            "restaurant" -> TYPE_RESTAURANT

            "cafe" -> TYPE_CAFE

            "bar" -> TYPE_BAR

            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    fun submitList(locationList: List<Places>, context: Context){
        items = locationList
        this.context = context
    }

    class RestaurantViewHolder constructor(itemView: View, context: Context): RecyclerView.ViewHolder(
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

            if(location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
            }
            if(location.website == null){
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener{
                val url = location.website
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                Toast.makeText(cont, "${this.locationName.text} clicked", Toast.LENGTH_SHORT).show()
                //TODO: Intent to new activity
            }
            }
        }
    class CafeViewHolder constructor(itemView: View, context: Context): RecyclerView.ViewHolder(
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

            if(location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
            }
            if(location.website == null){
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener{
                val url = location.website
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                Toast.makeText(cont, "${this.locationName.text} clicked", Toast.LENGTH_SHORT).show()
                //TODO: Intent to new activity
            }
        }
    }
    class BarViewHolder constructor(itemView: View, context: Context): RecyclerView.ViewHolder(
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

            if(location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
            }
            if(location.website == null){
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener{
                val url = location.website
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                Toast.makeText(cont, "${this.locationName.text} clicked", Toast.LENGTH_SHORT).show()
                //TODO: Intent to new activity
            }
        }
    }
    }

