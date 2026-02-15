package org.neo.yvstore.features.address.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.address.data.datasource.remote.AddressRemoteDatasource
import org.neo.yvstore.features.address.data.datasource.remote.AddressRemoteDatasourceImpl
import org.neo.yvstore.features.address.data.repository.AddressRepositoryImpl
import org.neo.yvstore.features.address.domain.repository.AddressRepository
import org.neo.yvstore.features.address.domain.usecase.AddAddressUseCase
import org.neo.yvstore.features.address.domain.usecase.DeleteAddressUseCase
import org.neo.yvstore.features.address.domain.usecase.GetAddressByIdUseCase
import org.neo.yvstore.features.address.domain.usecase.GetAddressesUseCase
import org.neo.yvstore.features.address.domain.usecase.RefreshAddressesUseCase
import org.neo.yvstore.features.address.presentation.screen.addAddress.AddAddressViewModel
import org.neo.yvstore.features.address.presentation.screen.addressList.AddressListViewModel

val addressModule = module {
    factoryOf(::AddressRemoteDatasourceImpl) { bind<AddressRemoteDatasource>() }
    factoryOf(::AddressRepositoryImpl) { bind<AddressRepository>() }

    factoryOf(::GetAddressesUseCase)
    factoryOf(::GetAddressByIdUseCase)
    factoryOf(::AddAddressUseCase)
    factoryOf(::DeleteAddressUseCase)
    factoryOf(::RefreshAddressesUseCase)

    viewModelOf(::AddressListViewModel)
    viewModelOf(::AddAddressViewModel)
}
