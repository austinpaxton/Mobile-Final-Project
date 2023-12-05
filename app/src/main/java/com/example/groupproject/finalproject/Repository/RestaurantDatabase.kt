package com.example.groupproject.finalproject.Repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the ToDoItem class
@Database(entities = arrayOf(Restaurant::class), version = 1, exportSchema = false)
public abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RestaurantDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RestaurantDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestaurantDatabase::class.java,
                    "geophotos_database"
                )
                    .addCallback(ResaruantRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class ResaruantRoomDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.restaurantDao())
                }
            }
        }

        suspend fun populateDatabase(restaurantDao: RestaurantDao) {
            // Delete all content here.
            restaurantDao.deleteAll()

            val junkData = getJunkData()
            for ((name, data) in junkData) {
                restaurantDao.insert(
                    Restaurant(
                        data.id,
                        null,
                        data.latitude,
                        data.longitude,
                        null,
                        name,
                        data.description,
                        data.cuisine,
                        data.rating
                    )
                )
            }
        }

        fun getJunkData(): Map<String, RestaurantData>{
            val restaurantDataList: MutableMap<String, RestaurantData> = HashMap()

            restaurantDataList["Freddy's"] = RestaurantData(1,36.105102, -94.206071, "Vintage-style chain for steakburgers, hot dogs & other fast-food staples, plus frozen custard.", "American", 2.5)
            restaurantDataList["Mr Taco Loco"] = RestaurantData(2, 36.045870, -94.165290, "Serves happy hour food · Serves great cocktails · Doesn't accept reservations", "Mexican", 4.0)


            return restaurantDataList
        }

        data class RestaurantData(val id: Int, val latitude: Double, val longitude: Double, val description: String, val cuisine: String, val rating: Double)
    }
}