package com.local.express.ui.adapter

interface IItemCard {
    fun changeTitle(newTitle:String,position:Int)
    fun cardClick(position:Int)
    fun cardLongClick(position:Int)
}