package org.neo.yvstore.features.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.auth.presentation.screens.login.LoginViewModel

val authModule = module {
    viewModelOf(::LoginViewModel)
}
