package com.example.groupproject.finalproject

import android.app.Application
import com.example.groupproject.finalproject.Repository.RestaurantRepository
import com.example.groupproject.finalproject.Repository.RestaurantDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RestaurantApplication:Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { RestaurantDatabase.getDatabase(this,applicationScope)}
    val repository by lazy{ RestaurantRepository(database.restaurantDao())}
}