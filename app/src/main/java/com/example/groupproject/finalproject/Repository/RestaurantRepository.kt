package com.example.groupproject.finalproject.Repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    val allRestaurants: Flow<Map<Int, Restaurant>> = restaurantDao.getRestaurants()

    @Suppress("RedudndantSuspendModifier")
    @WorkerThread
    suspend fun insert(restaurant: Restaurant){
        restaurantDao.insert(restaurant)
    }

    @Suppress("RedudndantSuspendModifier")
    @WorkerThread
    suspend fun getRestaurantById(restaurantId: Int): Restaurant {
        return restaurantDao.getRestaurantsId(restaurantId)
    }

    @Suppress("RedudndantSuspendModifier")
    @WorkerThread
    suspend fun deleteRestaurant(id: Int) {
        restaurantDao.deleteRestaurantById(id)
    }

    @Suppress("RedudndantSuspendModifier")
    @WorkerThread
    suspend fun updateRestaurant(restaurant: Restaurant) {
        restaurantDao.updateRestaurant(restaurant)
    }
}