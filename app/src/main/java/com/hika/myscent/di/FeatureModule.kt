package com.hika.myscent.di

import com.hika.myscent.features.auth.login.LoginViewModel
import com.hika.myscent.features.auth.register.RegisterViewModel
import com.hika.myscent.features.cart.CartViewModel
import com.hika.myscent.features.favorite.FavoriteViewModel
import com.hika.myscent.features.history.HistoryViewModel
import com.hika.myscent.features.history.payment.HistoryPaymentViewModel
import com.hika.myscent.features.home.HomeViewModel
import com.hika.myscent.features.payment.PaymentViewModel
import com.hika.myscent.features.product.ProductViewModel
import com.hika.myscent.features.profile.ProfileViewModel
import com.hika.myscent.features.review.ReviewViewModel
import com.hika.myscent.features.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProductViewModel(get(), get(), get(), get()) }
    viewModel { ReviewViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { PaymentViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { HistoryPaymentViewModel(get()) }
}