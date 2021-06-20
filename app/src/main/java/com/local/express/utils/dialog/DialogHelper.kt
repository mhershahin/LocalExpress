package com.local.express.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.local.express.R
import com.local.express.models.Item

class DialogHelper {

    fun changeUriOrURLDialog(context: Context, item: Item, iUriDialogCallBack: IUriDialogCallBack) {
        val dialog = Dialog(context, R.style.my_dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setContentView(R.layout.dialog_change_url)

        val urlUri: AppCompatEditText = dialog.findViewById(R.id.uri_url_et)
        val ok: AppCompatTextView = dialog.findViewById(R.id.dialog_ok)
        val cancel: AppCompatTextView = dialog.findViewById(R.id.dialog_cancel)

        urlUri.setText(item.uriOrUrl)
        ok.setOnClickListener {
            dialog.dismiss()
            item.uriOrUrl=urlUri.text.toString()
            iUriDialogCallBack.ok(item)
        }
        cancel.setOnClickListener {
            dialog.dismiss()
            iUriDialogCallBack.cancel()
        }

        dialog.show()
    }
    fun showAddDialog(context: Context, iAddDialogCallBack: IAddDialogCallBack) {
        val dialog = Dialog(context, R.style.my_dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setContentView(R.layout.dialog_add)

        val addTitleET: AppCompatEditText = dialog.findViewById(R.id.add_title_et)
        val ok: AppCompatTextView = dialog.findViewById(R.id.dialog_add_ok)
        val cancel: AppCompatTextView = dialog.findViewById(R.id.dialog_add_cancel)

        ok.setOnClickListener {
            dialog.dismiss()
            iAddDialogCallBack.gotoGallery(addTitleET.text.toString())
        }
        cancel.setOnClickListener {
            dialog.dismiss()
            iAddDialogCallBack.cancel()
        }
        dialog.show()
    }
}