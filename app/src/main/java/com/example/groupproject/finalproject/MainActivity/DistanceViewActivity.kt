package com.example.groupproject.finalproject.MainActivity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.groupproject.finalproject.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.location.Location
import com.example.groupproject.finalproject.RestaurantInfo

class DistanceViewActivity :  AppCompatActivity() {

    private lateinit var doneButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.distance_view)

        val currentLatitude = intent.getDoubleExtra("currentLatitude", 0.0)
        val currentLongitude = intent.getDoubleExtra("currentLongitude", 0.0)
        val currentLocation = Location("").apply {
            latitude = currentLatitude
            longitude = currentLongitude
        }


        doneButton = findViewById(R.id.doneButton)
        val restaurantInfoList = intent.getParcelableArrayListExtra<RestaurantInfo>("restaurantInfoList")
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (restaurantInfoList != null) {
            val adapter = DistanceAdapter(restaurantInfoList,currentLocation)
            adapter.updateData(currentLocation)
            recyclerView.adapter = adapter
        }

        doneButton.setOnClickListener {
            Log.d("DistanceActivity", "$restaurantInfoList")
            finish()
        }
    }
}