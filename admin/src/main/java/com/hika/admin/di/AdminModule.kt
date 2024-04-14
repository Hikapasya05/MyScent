package com.hika.admin.di

import com.hika.admin.features.home.HomeAdminViewModel
import com.hika.admin.features.product.ProductManagerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adminModule = module {
    viewModel { HomeAdminViewModel(get()) }
    viewModel { ProductManagerViewModel(get()) }
}