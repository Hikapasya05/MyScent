package com.hika.admin.di

import com.hika.admin.features.home.HomeAdminViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adminModule = module {
    viewModel { HomeAdminViewModel(get()) }
}