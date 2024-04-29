package com.hika.admin.di

import com.hika.admin.features.home.HomeAdminViewModel
import com.hika.admin.features.order.OrderAdminViewModel
import com.hika.admin.features.product.ProductManagerViewModel
import com.hika.admin.features.profile.ProfileAdminViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adminModule = module {
    viewModel { HomeAdminViewModel(get()) }
    viewModel { OrderAdminViewModel(get()) }
    viewModel { ProductManagerViewModel(get()) }
    viewModel { ProfileAdminViewModel(get()) }
}