package com.hika.myscent.di

import com.hika.myscent.features.auth.login.LoginViewModel
import com.hika.myscent.features.auth.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}