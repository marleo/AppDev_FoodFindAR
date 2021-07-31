package com.uni.foodfindar.ar

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.uni.foodfindar.R

class PlaceNode(
    val context: Context,
    val place: Place?
) : Node() {

    private var placeRenderable: ViewRenderable? = null
    private var textViewPlaceDistance: TextView? = null
    private var textViewPlaceName: TextView? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            return
        }

        if (placeRenderable != null) {
            return
        }

        ViewRenderable.builder()
            .setView(context, R.layout.place_view)
            .build()
            .thenAccept { renderable ->
                setRenderable(renderable)
                placeRenderable = renderable

                place?.let {
                    textViewPlaceDistance = renderable.view.findViewById(R.id.placeDistance)
                    textViewPlaceDistance?.text = it.distance
                    textViewPlaceName = renderable.view.findViewById(R.id.placeName)
                    textViewPlaceName?.text = it.name
                }
            }
    }
}