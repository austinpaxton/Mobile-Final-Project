package com.example.groupproject.finalproject.MainActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.groupproject.finalproject.R
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class RestaurantViewActivity : AppCompatActivity() {

    lateinit var editDescriptionView: EditText
    lateinit var editNameView: EditText
    lateinit var imageView: ImageView
    lateinit var ratingBar: RatingBar
    lateinit var editCuisineView: AutoCompleteTextView
    var cuisineAutoComplete = arrayOf("American", "Mexican", "Italian", "Coffee", "Barbecue")

    //onCreate method which will set onclick listener to get the view information
    public override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_view)


        val fileName = intent.getStringExtra("fileName")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val cuisine = intent.getStringExtra("cuisine")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val dateTime = intent.getStringExtra("dateTime")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        val id = intent.getStringExtra("id")

        editCuisineView = findViewById(R.id.cuisineTextView)
        val adapterArray = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item,cuisineAutoComplete)
        editCuisineView.setAdapter(adapterArray)
        editDescriptionView = findViewById(R.id.decriptionTextView)
        editNameView = findViewById(R.id.restaurantOneTextView)
        imageView = findViewById(R.id.imageView)
        ratingBar = findViewById(R.id.ratingBar)

        val saveButton = findViewById<Button>(R.id.saveBtn)

        if(dateTime == null)
        {
            val myUri: Uri? = Uri.parse(fileName)
            imageView.setImageURI(myUri)

            saveButton.setOnClickListener {
                val replyIntent = Intent()
                val currentTime = System.currentTimeMillis() / 1000
                replyIntent.putExtra(FILENAME, fileName)
                replyIntent.putExtra(DATETIME, currentTime)
                replyIntent.putExtra(DESCRIPTION, editDescriptionView.text.toString())
                replyIntent.putExtra(NAME, editNameView.text.toString())
                replyIntent.putExtra(CUISINE, editCuisineView.text.toString())
                replyIntent.putExtra(RATING, ratingBar.rating.toDouble())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }

        else
        {
            editDescriptionView.setText(description)
            editNameView.setText(name)
            editCuisineView.setText(cuisine)
            ratingBar.rating = rating?.toFloat() ?: 0.0f
            val myUri: Uri? = Uri.parse(fileName)
            imageView.setImageURI(myUri)

            saveButton.setOnClickListener {
                val replyIntent = Intent()
                val currentTime = System.currentTimeMillis() / 1000
                replyIntent.putExtra(FILENAME, fileName)
                replyIntent.putExtra(DESCRIPTION, editDescriptionView.text.toString())
                replyIntent.putExtra(NAME, editNameView.text.toString())
                replyIntent.putExtra(CUISINE, editCuisineView.text.toString())
                replyIntent.putExtra(RATING, ratingBar.rating.toDouble())
                replyIntent.putExtra(DATETIME, currentTime)
                replyIntent.putExtra(LATITUDE, longitude.toString())
                replyIntent.putExtra(LONGITUDE, latitude.toString())
                replyIntent.putExtra(ID, id.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    companion object {
        const val FILENAME = "com.example.groupproject.finalproject.FILENAME"
        const val DESCRIPTION = "com.example.groupproject.finalproject.DESCRIPTION"
        const val DATETIME = "com.example.groupproject.finalproject.DATIME"
        const val LATITUDE = "com.example.groupproject.finalproject.LATITUDE"
        const val LONGITUDE = "com.example.groupproject.finalproject.LONGITUDE"
        const val ID = "com.example.groupproject.finalproject.ID"
        const val NAME = "com.example.groupproject.finalproject.NAME"
        const val RATING = "com.example.groupproject.finalproject.RATING"
        const val CUISINE = "com.example.groupproject.finalproject.CUISINE"
    }
}