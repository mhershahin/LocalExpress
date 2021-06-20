package com.local.express.utils.dialog
import com.local.express.models.Item
interface IUriDialogCallBack {
    fun ok(item: Item)
    fun cancel()
}