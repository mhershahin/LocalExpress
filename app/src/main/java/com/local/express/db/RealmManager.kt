package com.local.express.db

import com.local.express.db.models.RealmItem
import com.local.express.models.Item
import com.local.express.utils.parsToItemList
import com.local.express.utils.parsToRealmItems
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RealmManager(private val myRealm: MyRealm)  {

   suspend fun addItemsToDB(items: MutableList<Item>) = withContext(Dispatchers.IO){
       //it is not best way save item in DB sorry ))
        removeAllItems()
        myRealm.getRealm().executeTransaction { realm ->
           for(myRealmItem in items.parsToRealmItems()) {
               val realmItem = realm.createObject(RealmItem::class.java)
               realmItem.title = myRealmItem.title
               realmItem.uriOrUrl = myRealmItem.uriOrUrl
           }
       }
   }

    suspend fun getItemsFromDB():MutableList<Item> = suspendCoroutine {
        val realm = myRealm.getRealm()
        val realmResults =realm.where(RealmItem::class.java).findAll()
        var myRealmList: MutableList<RealmItem> = realm.copyFromRealm(realmResults)
       it.resume(myRealmList.parsToItemList())
    }
    private fun removeAllItems() {
        var realm = myRealm.getRealm()
        realm.beginTransaction()
        var list:RealmResults<RealmItem>  = realm.where(RealmItem::class.java).findAll()
        list.deleteAllFromRealm()
        realm.commitTransaction()
    }
}