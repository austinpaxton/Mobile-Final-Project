package com.example.groupproject.finalproject.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_table")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name="filename") var filename:String?,
    @ColumnInfo(name="latitude") var latitude:Double?,
    @ColumnInfo(name="longitude") var longitude:Double?,
    @ColumnInfo(name="datetime") var datetime:Double?,
    @ColumnInfo(name="name") var name:String?,
    @ColumnInfo(name="description") var description:String?,
    @ColumnInfo(name = "rating") var rating: Double?

)