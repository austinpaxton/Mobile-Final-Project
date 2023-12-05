package com.example.groupproject.finalproject

import android.os.Parcel
import android.os.Parcelable

data class RestaurantInfo(
    val name: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantInfo> {
        override fun createFromParcel(parcel: Parcel): RestaurantInfo {
            return RestaurantInfo(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantInfo?> {
            return arrayOfNulls(size)
        }
    }
}
