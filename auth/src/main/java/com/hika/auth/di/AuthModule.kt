package com.hika.auth.di

import com.hika.auth.features.auth.login.LoginViewModel
import com.hika.auth.features.auth.register.RegisterViewModel
import com.hika.auth.features.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}