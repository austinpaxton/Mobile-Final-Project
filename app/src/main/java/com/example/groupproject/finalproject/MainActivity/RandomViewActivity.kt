package com.example.groupproject.finalproject.MainActivity


import android.os.Bundle
import android.widget.Button
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.groupproject.finalproject.R
import android.widget.TextView
import kotlin.random.Random

class RandomViewActivity :  AppCompatActivity() {

    private lateinit var randomButton: Button
    private lateinit var editRestaurantOneView: AutoCompleteTextView
    private lateinit var editRestaurantTwoView: AutoCompleteTextView
    private lateinit var editRestaurantThreeView: AutoCompleteTextView
    private lateinit var resultTextView: TextView
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_view)

        editRestaurantOneView = findViewById(R.id.restaurantOneTextView)
        editRestaurantTwoView = findViewById(R.id.restaurantTwoTextView)
        editRestaurantThreeView = findViewById(R.id.restaurantThreeTextView)
        resultTextView = findViewById(R.id.resultTextView)
        randomButton = findViewById(R.id.randomButton)
        val restaurantNames = intent.getStringArrayListExtra("restaurantNames")?.toMutableList() ?: mutableListOf()
        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item,restaurantNames.toList())
        editRestaurantOneView.setAdapter(adapterArray)
        editRestaurantTwoView.setAdapter(adapterArray)
        editRestaurantThreeView.setAdapter(adapterArray)

        randomButton.setOnClickListener{


            val restaurantOne = editRestaurantOneView.text.toString()
            val restaurantTwo = editRestaurantTwoView.text.toString()
            val restaurantThree = editRestaurantThreeView.text.toString()
            val restaurantList = listOf(restaurantOne, restaurantTwo, restaurantThree)

            // Check if any of the EditText views is empty
            if (restaurantList.any { it.isBlank() }) {
                resultTextView.text = "Please enter names for all restaurants"
            } else {
                val randomIndex = Random.nextInt(restaurantList.size)
                val randomRestaurant = restaurantList[randomIndex]
                resultTextView.text = "Randomly restaurant: $randomRestaurant"
            }

            // Make the resultTextView visible
            resultTextView.visibility = View.VISIBLE

            // Delay execution by 3 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                // Code to be executed after 5 seconds
                // For example, reset the resultTextView visibility
                finish()
            }, 3000)


            }
        }

    }

