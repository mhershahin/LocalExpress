package com.local.express.utils

import com.local.express.db.models.RealmItem
import com.local.express.models.Item
import io.realm.RealmList

fun MutableList<Item>.parsToRealmItems(): MutableList<RealmItem> {
    val realmItemList = mutableListOf<RealmItem>()
    for(item in this){
        realmItemList.add(RealmItem(item.title,item.uriOrUrl))
    }
    return realmItemList
}

fun MutableList<RealmItem>.parsToItemList():MutableList<Item>{
    val itemList = mutableListOf<Item>()
    for(realmItem in this){
        itemList.add(Item(realmItem.title,realmItem.uriOrUrl))
    }
    return itemList
}