package com.local.express.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.local.express.app.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val itemLiveData = repository.getItemsLiveData()
    fun addItemsToDB(){
        viewModelScope.launch{
            repository.saveItems()
        }
    }
    fun getItemsFromDB(context: Context){
        viewModelScope.launch{
            repository.getItems(context)
        }
    }
}