package com.ncueto.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.usecase.GetCurrentLocationUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherUseCase
import com.ncueto.weatherapp.domain.usecase.IsLocationEnabledUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val isLocationEnabledUseCase: IsLocationEnabledUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<WeatherEvent>()
    val events: SharedFlow<WeatherEvent> = _events.asSharedFlow()

    private var autoRefreshJob: Job? = null

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted, shouldRequestPermission = false) }
        if (granted) {
            loadWeather()
            startAutoRefresh()
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Permiso de ubicación denegado. Actívalo en configuración."
                )
            }
        }
    }

    private fun startAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(60_000) // Actualizar cada minuto
                if (_uiState.value.weather != null && _uiState.value.locationPermissionGranted) {
                    silentRefresh()
                }
            }
        }
    }

    // Visible for testing
    internal fun cancelAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    private fun silentRefresh() {
        viewModelScope.launch {
            // No mostrar loading, solo actualizar datos silenciosamente
            if (!isLocationEnabledUseCase()) return@launch

            when (val locationResult = getCurrentLocationUseCase().first()) {
                is Result.Success -> {
                    val location = locationResult.data
                    when (val weatherResult = getWeatherUseCase(location.latitude, location.longitude)) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(weather = weatherResult.data, error = null)
                            }
                        }
                        is Result.Error -> { /* Silently ignore errors during auto-refresh */ }
                    }
                }
                is Result.Error -> { /* Silently ignore errors during auto-refresh */ }
            }
        }
    }

    fun requestPermission() {
        viewModelScope.launch {
            _events.emit(WeatherEvent.RequestLocationPermission)
        }
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (!isLocationEnabledUseCase()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "GPS deshabilitado. Actívalo para ver el clima."
                    )
                }
                _events.emit(WeatherEvent.OpenLocationSettings)
                return@launch
            }

            when (val locationResult = getCurrentLocationUseCase().first()) {
                is Result.Success -> {
                    val location = locationResult.data
                    when (val weatherResult = getWeatherUseCase(location.latitude, location.longitude)) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    weather = weatherResult.data,
                                    error = null
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = weatherResult.exception.message
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = locationResult.exception.message
                        )
                    }
                }
            }
        }
    }

    fun searchCity(cityName: String) {
        if (cityName.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = getWeatherByCityUseCase(cityName)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            weather = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun refresh() {
        if (_uiState.value.locationPermissionGranted) {
            loadWeather()
        } else {
            requestPermission()
        }
    }

    fun onSettingsClick() {
        viewModelScope.launch {
            _events.emit(WeatherEvent.OpenSettings)
        }
    }

    fun onShareClick() {
        viewModelScope.launch {
            _events.emit(WeatherEvent.ShareWeather)
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoRefreshJob?.cancel()
    }
}