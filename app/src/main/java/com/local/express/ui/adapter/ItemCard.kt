package com.local.express.ui.adapter

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.loadingview.LoadingView
import com.local.express.R
import com.local.express.models.Item
import com.local.express.utils.extension.afterTextChanged
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ItemCard(private var context: Context, view: View) : RecyclerView.ViewHolder(view) {

private val loaderLayout: RelativeLayout = view.findViewById(R.id.loader_layout)
private val loader: LoadingView = view.findViewById(R.id.loader)
private val itemImg:AppCompatImageView = view.findViewById(R.id.item_img)
private val textView: AppCompatEditText = view.findViewById(R.id.title_et)

    fun bind(item: Item, iItemCard: IItemCard) {
        textView.setText(item.title)
        textView.afterTextChanged{ iItemCard.changeTitle(it,absoluteAdapterPosition)}
        itemImg.setOnClickListener{iItemCard.cardClick(absoluteAdapterPosition)}
        itemImg.setOnLongClickListener {
            iItemCard.cardLongClick(absoluteAdapterPosition)
            return@setOnLongClickListener false
        }
        showLoader()
        Picasso.get().
        load(item.uriOrUrl).
        into(itemImg,object :Callback{
            override fun onSuccess() {
                stopLoader()
            }
            override fun onError(e: Exception?) {
                itemImg.setImageResource(R.drawable.error)
                stopLoader()
            }
        })
    }
    private fun showLoader() {
        itemImg.visibility = View.GONE
        loaderLayout.visibility = View.VISIBLE
        loader.start()
    }
    private fun stopLoader() {
        loader.stop()
        loaderLayout.visibility = View.GONE
        itemImg.visibility = View.VISIBLE
    }
}