package com.example.groupproject.finalproject.MainActivity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.location.Location
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.finalproject.R
import com.example.groupproject.finalproject.RestaurantInfo
import androidx.core.text.TextUtilsCompat
class DistanceAdapter(private val originalRestaurantInfoList: List<RestaurantInfo>, private val currentLocation: Location) :
    RecyclerView.Adapter<DistanceAdapter.ViewHolder>() {

    private var sortedRestaurantInfoList: List<RestaurantInfo> = originalRestaurantInfoList.sortedBy {
        val restaurantLocation = Location("").apply {
            latitude = it.latitude
            longitude = it.longitude
        }
        currentLocation.distanceTo(restaurantLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantInfo = sortedRestaurantInfoList[position]
        holder.bind(restaurantInfo)
    }

    override fun getItemCount(): Int {
        return sortedRestaurantInfoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val distanceTextView: TextView = itemView.findViewById(R.id.tvDistance)

        fun bind(restaurantInfo: RestaurantInfo) {
            val restaurantLocation = Location("").apply {
                latitude = restaurantInfo.latitude
                longitude = restaurantInfo.longitude
            }
            // Convert distance to kilometers or miles as needed
            val distance = currentLocation.distanceTo(restaurantLocation) / 1609.344 // in miles

            // Assuming RestaurantInfo has a property like 'name'
            titleTextView.text = restaurantInfo.name
            distanceTextView.text = String.format("%.2f miles", distance)
        }
    }

    // Function to update the dataset and trigger sorting
    fun updateData(newCurrentLocation: Location) {
        currentLocation.set(newCurrentLocation)

        sortedRestaurantInfoList = originalRestaurantInfoList.sortedBy {
            val restaurantLocation = Location("").apply {
                latitude = it.latitude
                longitude = it.longitude
            }
            currentLocation.distanceTo(restaurantLocation)
        }

        notifyDataSetChanged()
    }
}