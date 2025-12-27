package com.ncueto.weatherapp.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: AppException) : Result<Nothing>()
}

sealed class AppException(override val message: String) : Exception(message) {
    data object NetworkError : AppException("Error de conexión. Verifica tu internet.")
    data object ServerError : AppException("Error del servidor. Intenta más tarde.")
    data object LocationPermissionDenied : AppException("Permiso de ubicación denegado.")
    data object LocationDisabled : AppException("GPS deshabilitado. Actívalo para continuar.")
    data object LocationUnavailable : AppException("No se pudo obtener la ubicación.")
    data class Unknown(override val message: String) : AppException(message)
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (AppException) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}