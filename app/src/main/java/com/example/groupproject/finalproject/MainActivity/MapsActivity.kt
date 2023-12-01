package com.example.groupproject.finalproject.MainActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration.*


import com.example.groupproject.finalproject.RestaurantApplication
import com.example.groupproject.finalproject.R
import com.example.groupproject.finalproject.Repository.Restaurant
import com.example.groupproject.finalproject.Util.replaceFragmentInActivity
import com.example.groupproject.bestlocationever.Util.*
import org.osmdroid.util.GeoPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MapsActivity : AppCompatActivity() {

    private lateinit var mapsFragment: OpenStreetMapFragment
    var currentPhotoPath:String = ""

    //Boolean to keep track of whether permissions have been granted
    private var locationPermissionEnabled:Boolean = false
    //Boolean to keep track of whether activity is currently requesting location Updates
    private var locationRequestsEnabled:Boolean = false
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var mCurrentLocation: Location
    private lateinit var mLocationCallback: LocationCallback

    private val newImageViewActivityRequestCode =1;

    //map section
    val takePictureResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(applicationContext, "No picture taken", Toast.LENGTH_LONG)
        }

        else
        {
            val imageViewIntent = Intent(this@MapsActivity, RestaurantViewActivity::class.java)
            val latitude = mCurrentLocation.latitude
            val longitude = mCurrentLocation.longitude
            imageViewIntent.putExtra("fileName", currentPhotoPath)
            imageViewIntent.putExtra("description", "")
            imageViewIntent.putExtra("cuisine", "")
            imageViewActivityLauncher.launch(imageViewIntent)
        }
    }

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Permission Granted")
        } else {
            Toast.makeText(this, "Location permissions not granted. Location disabled on map", Toast.LENGTH_LONG).show()
        }
    }

    private val mapsViewModel: MapsViewModel by viewModels {
        MapsViewModel.ToDoListViewModelFactory((application as RestaurantApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //Get a locationProviderClient object
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //Attempt to get the last known location
        // Will either require permission check or will return last known location
        // through the locationUtilCallback
        getLastLocation(this,locationProviderClient,locationUtilCallback)

        findViewById<FloatingActionButton>(R.id.addRestaurant).setOnClickListener{
            takeNewPhoto()
        }

        findViewById<FloatingActionButton>(R.id.randomRestaurant).setOnClickListener{
            val restaurantIntent = Intent(this@MapsActivity, RandomViewActivity::class.java)
            // Start the new activity
            startActivity(restaurantIntent)
        }


        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        checkForLocationPermission()

        //marker click listener
        supportFragmentManager.setFragmentResultListener("requestKey", this) {
                requestKey, bundle ->
                val result = bundle.getString("bundleKey")
                Log.d("MainActivity", "Marker has been clicked with $result")

                val restaurantsList = mapsViewModel.allRestaurants.value
                if (restaurantsList != null)
                {
                    for (restaurant in restaurantsList)
                    {
                        if (result != null)
                        {
                            if (restaurant.value.id == result.toIntOrNull())
                            {
                                val imageViewIntent = Intent(this@MapsActivity, RestaurantViewActivity::class.java)

                                imageViewIntent.putExtra("fileName", restaurant.value.filename.toString())
                                imageViewIntent.putExtra("description", restaurant.value.description.toString())
                                imageViewIntent.putExtra("name", restaurant.value.name.toString())
                                imageViewIntent.putExtra("cuisine", restaurant.value.cuisine.toString())
                                imageViewIntent.putExtra("rating", restaurant.value.rating)
                                imageViewIntent.putExtra("dateTime", restaurant.value.datetime.toString())
                                imageViewIntent.putExtra("latitude", restaurant.value.latitude.toString())
                                imageViewIntent.putExtra("longitude", restaurant.value.longitude.toString())
                                imageViewIntent.putExtra("id", restaurant.value.id.toString())
                                Log.d("MainActivity", "Rating $restaurant")
                                imageViewActivityLauncher.launch(imageViewIntent)
                            }
                        }

                        else
                        {
                            Log.d("MainActivity", "result null from fragment")
                        }
                    }
                }
        }

        //Get access to mapsFragment object
        mapsFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as OpenStreetMapFragment? ?:OpenStreetMapFragment.newInstance().also{
                replaceFragmentInActivity(it,R.id.fragmentContainerView)
        }

        //Begin observing data changes
        mapsViewModel.allRestaurants.observe(this){
            geoRestaurants->
            geoRestaurants.let {
                for(restaurants in geoRestaurants)
                {
                    val latitude = restaurants.value.latitude
                    val longitude = restaurants.value.longitude
                    val id = restaurants.value.id
                    var geoPoint:GeoPoint? = null

                    if(latitude!=null)
                    {
                        if(longitude!= null)
                        {
                            geoPoint = GeoPoint(latitude,longitude)
                        }
                    }

                    if(id != null && geoPoint!= null)
                    {
                        mapsFragment.addMarker(geoPoint,id)
                    }
                }
            }
        }
    }

    // Launcher for locationPermissions. To be launched when location permissions
    // are not available
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                locationPermissionEnabled = true
                startLocationRequests()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                locationPermissionEnabled = true
                startLocationRequests()
            }
            else -> {
            locationPermissionEnabled = false
            Toast.makeText(this,"Location Not Enabled",Toast.LENGTH_LONG)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationRequests()
    }

    override fun onStop(){
        super.onStop()
        if(locationRequestsEnabled){
            locationRequestsEnabled = false
            stopLocationUpdates(locationProviderClient,mLocationCallback)
        }
    }

    private fun startLocationRequests(){
        if(!locationRequestsEnabled){
            mLocationCallback = createLocationCallback(locationUtilCallback)
            locationRequestsEnabled = createLocationRequest(this,locationProviderClient,mLocationCallback)
        }
    }


    private val locationUtilCallback = object: LocationUtilCallback {
        override fun requestPermissionCallback() {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        override fun locationUpdatedCallback(location: Location) {
            mCurrentLocation = location
            Log.d("MainActivity","Location is [Lat: ${location.latitude}, Long: ${location.longitude}]")
        }
    }

    private fun checkForLocationPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    //Photo section
    private fun takeNewPhoto() {
        getLastLocation(this,locationProviderClient,locationUtilCallback)
        val picIntent = Intent().setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        if(picIntent.resolveActivity(packageManager) != null)
        {
            val filepath: String = createFilePath()
            val myFile: File = File(filepath)
            currentPhotoPath = filepath
            val photoUri = FileProvider.getUriForFile(this,"com.example.groupproject.finalproject.fileprovider",myFile)
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            takePictureResultLauncher.launch(picIntent)
        }
    }

    private fun createFilePath(): String {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intent
        return image.absolutePath
    }

    // ImageViewActivity Section
    private val imageViewActivityLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val intentData = result.data
        if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(
                applicationContext,
                "Did not save update",
                Toast.LENGTH_LONG
            ).show()

        }

        else
        {
            var fileName: String = ""
            var description: String = ""
            var dateTime: Double = 0.0
            var name: String = ""
            var cuisine: String = ""
            var rating: Double = 0.0
            val latitude = mCurrentLocation.latitude
            val longitude = mCurrentLocation.longitude
            var itemID: Int? = null

            intentData?.getStringExtra(RestaurantViewActivity.FILENAME)?.let { FileName ->
                fileName = FileName
            }
            intentData?.getStringExtra(RestaurantViewActivity.DESCRIPTION)?.let { Description ->
                description = Description
            }
            intentData?.getStringExtra(RestaurantViewActivity.NAME)?.let { Name ->
                name = Name
            }
            intentData?.getStringExtra(RestaurantViewActivity.CUISINE)?.let { Cuisine ->
                cuisine = Cuisine
            }
            intentData?.getDoubleExtra(RestaurantViewActivity.RATING, 0.0)?.let { Rating ->
                rating = Rating
            }
            intentData?.getStringExtra(RestaurantViewActivity.DATETIME)?.let { DateTime ->
                dateTime = DateTime.toDouble()
            }
            intentData?.getStringExtra(RestaurantViewActivity.ID)?.let { id ->
                if(id != "null") {
                    itemID = id.toInt()
                }
            }
            val insertData = Restaurant(itemID, fileName, latitude, longitude, dateTime, name, description, cuisine ,rating)
            mapsViewModel.insertRec(insertData)
        }
    }

    private fun createNewImageActivity(myItem: Restaurant){
        val intent = Intent(this@MapsActivity,RestaurantViewActivity::class.java)
        intent.putExtra("fileName", myItem.filename)
        intent.putExtra("description", myItem.description)
        startActivityForResult(intent,newImageViewActivityRequestCode)
    }
}