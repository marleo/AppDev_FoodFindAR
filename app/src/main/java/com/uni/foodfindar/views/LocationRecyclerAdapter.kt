package com.uni.foodfindar.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import com.uni.foodfindar.ar.ArView


class LocationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Places> = ArrayList()
    private var filteredItems: List<Places> = ArrayList<Places>()
    private lateinit var context: Context
    private lateinit var pref: SharedPreferences

    private var restaurant: Boolean = false
    private var cafe: Boolean = false
    private var bar: Boolean = false
    private var sliderPos1: Boolean = false
    private var sliderPos2: Boolean = false
    private var sliderPos3: Boolean = false
    private var sliderPos4: Boolean = false

    companion object {
        private const val TYPE_RESTAURANT = 0
        private const val TYPE_CAFE = 1
        private const val TYPE_BAR = 2
        private const val TYPE_EMPTY = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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
            TYPE_EMPTY -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.nearby_list_item_empty, parent, false)
                EmptyViewHolder(view, context)
            }
            else -> throw  IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        pref = context.getSharedPreferences(context.resources.getString(R.string.preferences_key), 0)

        restaurant = pref.getBoolean("restaurant", false)
        cafe = pref.getBoolean("cafe", false)
        bar = pref.getBoolean("bar", false)

        Log.i("Filtered List", "${filteredItems.size}")

        sliderPos1 = pref.getBoolean("sliderPos1", false)
        sliderPos2 = pref.getBoolean("sliderPos2", false)
        sliderPos3 = pref.getBoolean("sliderPos3", false)
        sliderPos4 = pref.getBoolean("sliderPos4", false)



        when (holder) {
            is RestaurantViewHolder -> {
                if (restaurant)
                    holder.bind(items[position])
            }
            is CafeViewHolder -> {
                if (cafe)
                    holder.bind(items[position])
            }
            is BarViewHolder -> {
                if (bar)
                    holder.bind(items[position])
            }
            is EmptyViewHolder -> {

            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = items[position]

        return when (comparable.amenity) {
            "restaurant" -> {
                if (restaurant) {
                    when {
                        sliderPos1 && comparable.distance!! <= 0.5 -> TYPE_RESTAURANT
                        sliderPos2 && comparable.distance!! <= 1.0 -> TYPE_RESTAURANT
                        sliderPos3 && comparable.distance!! <= 1.5 -> TYPE_RESTAURANT
                        sliderPos4 && comparable.distance!! <= 2.0 -> TYPE_RESTAURANT
                        else -> TYPE_EMPTY
                    }
                } else
                    TYPE_EMPTY
            }

            "cafe" -> {
                if (cafe) {
                    when {
                        sliderPos1 && comparable.distance!! <= 0.5 -> TYPE_CAFE
                        sliderPos2 && comparable.distance!! <= 1.0 -> TYPE_CAFE
                        sliderPos3 && comparable.distance!! <= 1.5 -> TYPE_CAFE
                        sliderPos4 && comparable.distance!! <= 2.0 -> TYPE_CAFE
                        else -> TYPE_EMPTY
                    }
                } else
                    TYPE_EMPTY
            }

            "bar" -> {
                if (bar) {
                    when {
                        sliderPos1 && comparable.distance!! <= 0.5 -> TYPE_BAR
                        sliderPos2 && comparable.distance!! <= 1.0 -> TYPE_BAR
                        sliderPos3 && comparable.distance!! <= 1.5 -> TYPE_BAR
                        sliderPos4 && comparable.distance!! <= 2.0 -> TYPE_BAR
                        else -> TYPE_EMPTY
                    }
                } else
                    TYPE_EMPTY
            }

            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    fun submitList(locationList: List<Places>, context: Context) {
        items = locationList
        this.context = context
    }


    class RestaurantViewHolder constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(
            itemView
    ) {

        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationDistance: TextView = itemView.findViewById(R.id.location_distance)
        private var locationDistanceString: String = ""
        private var locationWebsite: ImageView = itemView.findViewById(R.id.website)
        private var cont: Context = context

        fun bind(location: Places) {
            locationName.text = location.name
            locationDistanceString = (location.distance?.times(1000)?.toInt()).toString() + "m"
            locationDistance.text = locationDistanceString
            var url: String? = null

            if (location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
                url = prepareLink(location.website)
            }
            if (location.website == null) {
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                val intent = Intent(cont, ArView::class.java)
                val bundle = Bundle()
                bundle.putParcelable("Place", location)
                intent.putExtras(bundle)
                startActivity(cont, intent, null)
            }
        }

        private fun prepareLink(url: String?): String?{
            val pattern: String = "^(http|https|ftp)://.*$"
            return if(url?.matches(Regex(pattern)) == true)
                url
            else
                "https://$url"
        }
    }

    class CafeViewHolder constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(
            itemView
    ) {

        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationDistance: TextView = itemView.findViewById(R.id.location_distance)
        private var locationDistanceString: String = ""
        private var locationWebsite: ImageView = itemView.findViewById(R.id.website)
        private var cont: Context = context

        fun bind(location: Places) {
            locationName.text = location.name
            locationDistanceString = (location.distance?.times(1000)?.toInt()).toString() + "m"
            locationDistance.text = locationDistanceString
            var url: String? = null

            if (location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
                url = prepareLink(location.website)
            }
            if (location.website == null) {
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                val intent = Intent(cont, ArView::class.java)
                val bundle = Bundle()
                bundle.putParcelable("Place", location)
                intent.putExtras(bundle)
                startActivity(cont, intent, null)
            }
        }

        private fun prepareLink(url: String?): String?{
            val pattern: String = "^(http|https|ftp)://.*$"
            return if(url?.matches(Regex(pattern)) == true)
                url
            else
                "https://$url"
        }
    }

    class BarViewHolder constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(
            itemView
    ) {

        private val locationName: TextView = itemView.findViewById(R.id.location_name)
        private val locationDistance: TextView = itemView.findViewById(R.id.location_distance)
        private var locationDistanceString: String = ""
        private var locationWebsite: ImageView = itemView.findViewById(R.id.website)
        private var cont: Context = context

        fun bind(location: Places) {
            locationName.text = location.name
            locationDistanceString = (location.distance?.times(1000)?.toInt()).toString() + "m"
            locationDistance.text = locationDistanceString
            var url: String? = null

            if (location.website != null && location.website != "null") { //weird code
                Log.i("Websites", "" + location.name + " " + location.website)
                locationWebsite.visibility = View.VISIBLE
                url = prepareLink(location.website)
            }
            if (location.website == null) {
                locationWebsite.visibility = View.INVISIBLE
            }

            locationWebsite.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(cont, i, null)
            }

            itemView.setOnClickListener {
                val intent = Intent(cont, ArView::class.java)
                val bundle = Bundle()
                bundle.putParcelable("Place", location)
                intent.putExtras(bundle)
                startActivity(cont, intent, null)
            }
        }

        private fun prepareLink(url: String?): String?{
            val pattern: String = "^(http|https|ftp)://.*$"
            return if(url?.matches(Regex(pattern)) == true)
                url
            else
                "https://$url"
        }
    }
    class EmptyViewHolder constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView)
}

