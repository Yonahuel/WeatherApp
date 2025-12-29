package com.ncueto.weatherapp.di

import com.ncueto.weatherapp.data.remote.api.WeatherApiService
import com.ncueto.weatherapp.data.remote.api.WeatherApiServiceImpl
import com.ncueto.weatherapp.domain.repository.LocationRepository
import com.ncueto.weatherapp.domain.repository.LocationRepositoryImpl
import com.ncueto.weatherapp.domain.repository.WeatherRepository
import com.ncueto.weatherapp.domain.repository.WeatherRepositoryImpl
import com.ncueto.weatherapp.domain.usecase.GetCurrentLocationUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherUseCase
import com.ncueto.weatherapp.domain.usecase.IsLocationEnabledUseCase
import com.ncueto.weatherapp.presentation.viewmodel.WeatherViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    single<WeatherApiService> { WeatherApiServiceImpl(get()) }
}

val repositoryModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext()) }
}

val useCaseModule = module {
    factory { GetWeatherUseCase(get()) }
    factory { GetWeatherByCityUseCase(get()) }
    factory { GetCurrentLocationUseCase(get()) }
    factory { IsLocationEnabledUseCase(get()) }
}

val viewModelModule = module {
    viewModel {
        WeatherViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
