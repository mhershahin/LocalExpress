package com.local.express.app

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.local.express.R
import com.local.express.db.RealmManager
import com.local.express.models.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class Repository(private val realmManager: RealmManager) {

    private val itemsLiveData = MutableLiveData<MutableList<Item>>()
    private val itemsList = mutableListOf<Item>()
    fun getItemsLiveData(): MutableLiveData<MutableList<Item>> {
        return itemsLiveData
    }
    suspend fun getItems(context: Context) = coroutineScope {
        // first get from DB
        if (itemsList.isEmpty()) {
            var dbItems = withContext(Dispatchers.Default) { realmManager.getItemsFromDB() }
            itemsList.addAll(dbItems)
        }
        //if DB is Empty create Default Items
        if (itemsList.isEmpty()) {
            itemsList.addAll(getDefaultItems(context))
        }
        itemsLiveData.postValue(itemsList)
    }
    suspend fun saveItems()= coroutineScope{
        realmManager.addItemsToDB(itemsList)
    }

    fun changeItems(position:Int,title:String,imgUri:String) {
        if (position == -1) {
            val newItem = Item(title,imgUri)
            itemsList.add(0, newItem)
        } else {
            itemsList[position].uriOrUrl = imgUri
        }
        itemsLiveData.postValue(itemsList)
    }
    fun changeItemTitle(newTitle: String, position: Int) {
        itemsList[position].title = newTitle
    }
    fun addChangeUriOrURl(item: Item, position: Int) {
        itemsList[position] = item
        itemsLiveData.postValue(itemsList)
    }
    private fun getDefaultItems(context: Context): MutableList<Item> {
        val items = mutableListOf<Item>()
        val titleString = context.getString(R.string.title)
        for (i: Int in 1..200) {
            var random: Int = (0..3).random()
            val uri = when (random) {
                0 -> {
                    Uri.parse("android.resource://${context.packageName}/drawable/banana")
                        .toString()
                }
                1 -> {
                    Uri.parse("android.resource://${context.packageName}/drawable/cabbage")
                        .toString()
                }
                2 -> {
                    Uri.parse("android.resource://${context.packageName}/drawable/tomato")
                        .toString()
                }
                else -> {
                    Uri.parse("android.resource://${context.packageName}/drawable/banana")
                        .toString()
                }
            }
            items.add(Item("$titleString $i", uri))
        }
        return items
    }
}