package com.local.express.app

import com.local.express.db.MyRealm
import com.local.express.db.RealmManager
import com.local.express.ui.MainViewModel
import com.local.express.utils.dialog.DialogHelper
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


@InternalCoroutinesApi
val appModule = module {
    single { MyRealm(get()) }
    single { RealmManager(get()) }
    single { Repository(get()) }
    single { DialogHelper() }

    viewModel { MainViewModel(get()) }
}