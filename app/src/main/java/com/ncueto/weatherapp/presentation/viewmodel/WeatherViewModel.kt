package com.ncueto.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncueto.weatherapp.domain.model.Result
import com.ncueto.weatherapp.domain.usecase.GetCurrentLocationUseCase
import com.ncueto.weatherapp.domain.usecase.GetWeatherUseCase
import com.ncueto.weatherapp.domain.usecase.IsLocationEnabledUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val isLocationEnabledUseCase: IsLocationEnabledUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<WeatherEvent>()
    val events: SharedFlow<WeatherEvent> = _events.asSharedFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted, shouldRequestPermission = false) }
        if (granted) {
            loadWeather()
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Permiso de ubicación denegado. Actívalo en configuración"
                )
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
                        error = "GPS deshabilitado. Actívalo para ver el clima"
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

    fun refresh() {
        if (_uiState.value.locationPermissionGranted) {
            loadWeather()
        } else {
            requestPermission()
        }
    }
}