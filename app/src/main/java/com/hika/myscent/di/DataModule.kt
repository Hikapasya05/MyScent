package com.hika.myscent.di

import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hika.myscent.data.local.ScentDatabase
import com.hika.myscent.data.repository.auth.AuthRepository
import com.hika.myscent.data.repository.auth.AuthRepositoryImpl
import com.hika.myscent.data.repository.cart.CartRepository
import com.hika.myscent.data.repository.cart.CartRepositoryImpl
import com.hika.myscent.data.repository.favorite.FavoriteRepository
import com.hika.myscent.data.repository.favorite.FavoriteRepositoryImpl
import com.hika.myscent.data.repository.history.HistoryRepository
import com.hika.myscent.data.repository.history.HistoryRepositoryImpl
import com.hika.myscent.data.repository.perfume.PerfumeRepository
import com.hika.myscent.data.repository.perfume.PerfumeRepositoryImpl
import com.hika.myscent.data.repository.review.ReviewRepository
import com.hika.myscent.data.repository.review.ReviewRepositoryImpl
import com.hika.myscent.data.repository.user.UserRepository
import com.hika.myscent.data.repository.user.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ScentDatabase::class.java, "myscent.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    factory { get<ScentDatabase>().scentDao() }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PerfumeRepository> { PerfumeRepositoryImpl(get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
    single<CartRepository> { CartRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<HistoryRepository> { HistoryRepositoryImpl(get(), get(), get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get(), get()) }
}