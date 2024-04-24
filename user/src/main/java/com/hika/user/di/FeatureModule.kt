package com.hika.user.di

import com.hika.user.features.cart.CartViewModel
import com.hika.user.features.favorite.FavoriteViewModel
import com.hika.user.features.history.HistoryViewModel
import com.hika.user.features.history.payment.HistoryPaymentViewModel
import com.hika.user.features.home.HomeViewModel
import com.hika.user.features.payment.PaymentViewModel
import com.hika.user.features.product.ProductViewModel
import com.hika.user.features.profile.ProfileViewModel
import com.hika.user.features.review.ReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
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