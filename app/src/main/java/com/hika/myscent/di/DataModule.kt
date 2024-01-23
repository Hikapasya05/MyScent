package com.hika.myscent.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hika.myscent.data.repository.auth.AuthRepository
import com.hika.myscent.data.repository.auth.AuthRepositoryImpl
import com.hika.myscent.data.repository.perfume.PerfumeRepository
import com.hika.myscent.data.repository.perfume.PerfumeRepositoryImpl
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PerfumeRepository> { PerfumeRepositoryImpl(get()) }
}