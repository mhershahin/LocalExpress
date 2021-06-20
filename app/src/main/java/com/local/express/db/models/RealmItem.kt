package com.local.express.db.models
import io.realm.RealmObject

open class RealmItem(var title: String="", var uriOrUrl: String="") : RealmObject()
