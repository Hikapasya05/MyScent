package com.hika.myscentapp.di

import com.hika.admin.navigation.AdminNavigation
import com.hika.auth.navigation.AuthNavigation
import com.hika.myscentapp.navigator.AdminNavigator
import com.hika.myscentapp.navigator.AuthNavigator
import com.hika.myscentapp.navigator.UserNavigator
import com.hika.user.navigation.UserNavigation
import org.koin.dsl.module

val navigatorModule = module {
    single<AuthNavigation> { AuthNavigator() }
    single<UserNavigation> { UserNavigator(get()) }
    single<AdminNavigation> { AdminNavigator(get()) }
}