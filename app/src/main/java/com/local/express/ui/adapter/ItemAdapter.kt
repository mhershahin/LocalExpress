package com.local.express.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.local.express.R
import com.local.express.models.Item

class ItemAdapter(private val context:Context,private val itemList: MutableList<Item>,private val iItemCard: IItemCard) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   //it make easy to add new Cards
    private val ITEM_CARD = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_CARD->{
                val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
                ItemCard(context,viewItem)
            }else ->{
                val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
                ItemCard(context,viewItem)
            }
        }
    }
    override fun getItemCount(): Int {
      return itemList.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         when(holder){
             is ItemCard ->{
                 holder.bind(itemList[position],iItemCard)
             }
         }
    }
    override fun getItemViewType(position: Int): Int {
        return ITEM_CARD
    }
}