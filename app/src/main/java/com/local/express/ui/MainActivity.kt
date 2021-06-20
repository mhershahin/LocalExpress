package com.local.express.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.local.express.R
import com.local.express.app.Repository
import com.local.express.models.Item
import com.local.express.ui.adapter.IItemCard
import com.local.express.ui.adapter.ItemAdapter
import com.local.express.utils.dialog.DialogHelper
import com.local.express.utils.dialog.IAddDialogCallBack
import com.local.express.utils.dialog.IUriDialogCallBack
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IItemCard {
    private val mainViewModel: MainViewModel by viewModel()
    private val repository: Repository by inject()
    private val dialogHelper: DialogHelper by inject()

    private val itemList = mutableListOf<Item>()
    private val addImg: AppCompatImageView by lazy { this.findViewById(R.id.add_img) }
    private val itemRecycler: RecyclerView by lazy { this.findViewById(R.id.item_recycler) }
    private val adapter: ItemAdapter by lazy { ItemAdapter(this, itemList, this) }

    //for changing or adding Item
    //if itemPosition =-1 mean add new Item
    private var isChangedItem = false
    private var itemPosition = -1
    private var itemTitle = ""

    //Start Activity for result new way
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                     repository.changeItems(itemPosition,itemTitle,result.data?.data.toString())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycler()
        mainViewModel.getItemsFromDB(this)
        mainViewModel.itemLiveData.observe(this, {
            itemList.clear()
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
        })
        addImg.setOnClickListener {
            isChangedItem = false
            checkIsHavePermissions()
        }
    }
    private fun initRecycler() {
        itemRecycler.layoutManager = GridLayoutManager(this, 2)
        itemRecycler.setHasFixedSize(false)
        itemRecycler.clearFocus()
        itemRecycler.adapter = adapter
    }

    override fun changeTitle(newTitle: String, position: Int) {
        itemList[position].title = newTitle
        repository.changeItemTitle(newTitle,position)
    }
    override fun cardClick(position: Int) {
        dialogHelper.changeUriOrURLDialog(this, itemList[position], object : IUriDialogCallBack {
            override fun ok(item: Item) {
                repository.addChangeUriOrURl(item, position)
            }
            override fun cancel() {
            }
        })
    }
    override fun cardLongClick(position: Int) {
        isChangedItem = true
        itemPosition = position
        itemTitle = itemList[position].title
        checkIsHavePermissions()
    }
    private fun chooseImg() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, getString(R.string.select_picture)))
    }
    private fun openAddDialog() {
        dialogHelper.showAddDialog(this,object :IAddDialogCallBack{
            override fun gotoGallery(title: String) {
                itemPosition =-1
                itemTitle = title
                chooseImg()
            }
            override fun cancel() {

            }
        })
    }
    //Permission
    private val REQUEST_CODE_ASK_PERMISSIONS = 101
    private val permissions = mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray()
    private fun checkIsHavePermissions() {
        var permissionGradientWrite =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionGradientWrite == PackageManager.PERMISSION_GRANTED) {
            if(isChangedItem){
                chooseImg()
            }else{
               openAddDialog()
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS && grantResults.isNotEmpty()) {
            for (permissionsIndex in grantResults.indices) {
                if (grantResults[permissionsIndex] == PackageManager.PERMISSION_GRANTED) {
                    if(isChangedItem){
                        chooseImg()
                    }else{
                        openAddDialog()
                    }
                } else {
                    Toast.makeText(this, "NO PERMISSION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        mainViewModel.addItemsToDB()
        super.onStop()
    }
}