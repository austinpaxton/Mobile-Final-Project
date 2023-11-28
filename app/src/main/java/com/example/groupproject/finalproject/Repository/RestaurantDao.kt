package com.example.groupproject.finalproject.Repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @MapInfo(keyColumn = "id")
    @Query("SELECT * FROM restaurant_table order by id ASC")
    fun getRestaurants(): Flow<Map<Int, Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)

    @Query("DELETE FROM restaurant_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM restaurant_table WHERE id = :id")
    suspend fun getRestaurantsId(id:Int): Restaurant

    @Query("DELETE FROM restaurant_table WHERE id=:id")
    suspend fun deleteRestaurantById(id: Int)

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)


}