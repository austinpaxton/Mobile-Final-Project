package com.example.groupproject.finalproject.MainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.finalproject.R
import com.example.groupproject.finalproject.RestaurantInfo


class CuisineAdapter(private var restaurantList: MutableList<RestaurantInfo>) : RecyclerView.Adapter<CuisineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun updateData(newList: List<RestaurantInfo>) {
        restaurantList.clear()
        restaurantList.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val cuisineTextView: TextView = itemView.findViewById(R.id.tvDistance)

        fun bind(restaurant: RestaurantInfo) {
            nameTextView.text = restaurant.name
            cuisineTextView.text = restaurant.rating.toString()
        }
    }
}