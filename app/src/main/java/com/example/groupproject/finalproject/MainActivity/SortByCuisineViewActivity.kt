package com.example.groupproject.finalproject.MainActivity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.finalproject.R
import com.example.groupproject.finalproject.RestaurantInfo
import android.content.Intent

class SortByCuisineViewActivity : AppCompatActivity() {

    private lateinit var doneButton: Button
    private lateinit var sortButton: Button
    lateinit var editCuisineView: AutoCompleteTextView
    var cuisineAutoComplete = arrayOf("American", "Mexican", "Italian", "Coffee", "Barbecue")

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CuisineAdapter
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cuisine_view)
        editCuisineView = findViewById(R.id.cuisineSortAutoComplete)
        doneButton = findViewById(R.id.doneButton)
        sortButton = findViewById(R.id.sortButton)
        recyclerView = findViewById(R.id.recyclerview)

        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, cuisineAutoComplete)
        editCuisineView.setAdapter(adapterArray)

        val restaurantInfoList = intent.getParcelableArrayListExtra<RestaurantInfo>("restaurantInfoList")

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CuisineAdapter(restaurantInfoList?.toMutableList() ?: mutableListOf())
        recyclerView.adapter = adapter

        sortButton.setOnClickListener {
            val selectedCuisine = editCuisineView.text.toString()
            val filteredRestaurantList = restaurantInfoList?.filter { it.cuisine == selectedCuisine }

            if (filteredRestaurantList != null && filteredRestaurantList.isNotEmpty()) {
                adapter.updateData(filteredRestaurantList)
            } else {
                // Handle the case where the filteredRestaurantList is empty or null
            }
        }

        doneButton.setOnClickListener{
            finish()
        }
    }

}