package org.neo.yvstore.features.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.auth.presentation.screens.login.LoginViewModel
import org.neo.yvstore.features.auth.presentation.screens.signup.SignUpViewModel

val authModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}
