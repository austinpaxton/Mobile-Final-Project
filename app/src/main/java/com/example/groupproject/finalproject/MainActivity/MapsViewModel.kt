package com.example.groupproject.finalproject.MainActivity

import androidx.lifecycle.*
import com.example.groupproject.finalproject.Repository.Restaurant
import com.example.groupproject.finalproject.Repository.RestaurantRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: RestaurantRepository): ViewModel() {

    val allRestaurants: LiveData<Map<Int, Restaurant>> = repository.allRestaurants.asLiveData()

    fun insertRec(photoItem: Restaurant)= viewModelScope.launch{
        repository.insert(photoItem)
    }
    class ToDoListViewModelFactory(private val repository: RestaurantRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MapsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MapsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}