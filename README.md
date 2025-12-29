# Weather App

Aplicación de clima para Android que muestra información meteorológica en tiempo real utilizando la API de OpenWeather.

## Requisitos

- **Android Studio**: Hedgehog (2023.1.1) o superior
- **JDK**: 11
- **Android SDK mínimo**: API 24 (Android 7.0 Nougat)
- **Android SDK target**: API 36 (Android 16)

## Configuración

1. Clonar el repositorio

2. Configurar la API Key de OpenWeather:
    - Crear una cuenta en [OpenWeather](https://openweathermap.org/api)
    - Obtener una API Key gratuita
    - Crear el archivo `local.properties` en la raíz del proyecto (si no existe)
    - Agregar la línea:
      ```
      OPENWEATHER_API_KEY=tu_api_key_aquí
      ```

3. Sincronizar el proyecto con Gradle

## Compilar y ejecutar

### Desde Android Studio
1. Abrir el proyecto en Android Studio
2. Esperar a que Gradle sincronice las dependencias
3. Seleccionar un dispositivo o emulador
4. Ejecutar con `Run > Run 'app'` o presionar `Shift + F10`

### Desde línea de comandos
```bash
# Compilar debug APK
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# Ejecutar tests unitarios
./gradlew test
```

## Arquitectura

La aplicación implementa **Clean Architecture** con el patrón **MVVM** (Model-View-ViewModel), organizada en tres capas principales:

```
app/src/main/java/com/example/weatherapp/
├── data/                    # Capa de datos
│   ├── remote/
│   │   ├── api/            # Servicios de red (Ktor)
│   │   └── dto/            # Data Transfer Objects y mappers
│   └── repository/         # Implementaciones de repositorios
├── domain/                  # Capa de dominio
│   ├── model/              # Modelos de negocio
│   ├── repository/         # Interfaces de repositorios
│   └── usecase/            # Casos de uso
├── presentation/            # Capa de presentación
│   ├── ui/
│   │   ├── components/     # Componentes reutilizables
│   │   ├── screens/        # Pantallas
│   │   ├── navigation/     # Navegación
│   │   └── theme/          # Tema y estilos
│   └── viewmodel/          # ViewModels
└── di/                      # Inyección de dependencias
```

### Flujo de datos

```
UI (Compose) → ViewModel → UseCase → Repository → API/DataSource
                  ↓
              StateFlow
                  ↓
            UI actualizada
```

## Decisiones técnicas

### Librerías elegidas

| Librería | Propósito | Justificación |
|----------|-----------|---------------|
| **Jetpack Compose** | UI declarativa | Framework moderno de UI recomendado por Google, reduce boilerplate y facilita el mantenimiento |
| **Ktor Client** | Cliente HTTP | Librería Kotlin-first, ligera y con soporte nativo para coroutines. Alternativa moderna a Retrofit |
| **Koin** | Inyección de dependencias | Configuración simple y declarativa en Kotlin DSL, sin generación de código |
| **Kotlin Coroutines + Flow** | Programación asíncrona | Manejo reactivo del estado, integración nativa con Compose |
| **Compose Navigation** | Navegación | Solución oficial para navegación en Compose |
| **MockK** | Testing | Framework de mocking para Kotlin con soporte completo de coroutines |

### Estructura del proyecto

- **Separación por capas**: Cada capa tiene responsabilidades bien definidas y depende solo de las capas internas (Dependency Rule)
- **Repository Pattern**: Abstrae el origen de los datos, permitiendo cambiar la implementación sin afectar la lógica de negocio
- **UseCase por acción**: Cada caso de uso representa una única acción del usuario, facilitando testing y reutilización
- **StateFlow para UI State**: Estado inmutable y unidireccional, evitando inconsistencias en la UI

### Manejo de errores

Se implementó un modelo `Result<T>` sellado para manejar estados de éxito y error de forma type-safe:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: AppException) : Result<Nothing>()
}
```

### Seguridad

- La API Key se almacena en `local.properties` (excluido del control de versiones)
- Se inyecta en tiempo de compilación via BuildConfig
- No se expone en el código fuente

### Funcionalidades implementadas

- Obtención del clima por geolocalización (GPS)
- Búsqueda de clima por nombre de ciudad
- Auto-refresh cada 60 segundos
- Manejo de permisos de ubicación
- Estados de carga, error y vacío
- UI responsive con soporte para diferentes tamaños de pantalla

## Testing

La aplicación incluye tests unitarios para:

- **ViewModels**: Verifican el manejo de estados y la interacción con casos de uso
- **UseCases**: Verifican la lógica de negocio
- **Repositories**: Verifican el mapeo de datos y manejo de errores

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests con reporte
./gradlew testDebugUnitTest
```

## Estructura de archivos de test

```
app/src/test/java/com/example/weatherapp/
├── data/repository/
│   └── WeatherRepositoryImplTest.kt
├── domain/usecase/
│   ├── GetWeatherUseCaseTest.kt
│   └── GetWeatherByCityUseCaseTest.kt
└── presentation/viewmodel/
    └── WeatherViewModelTest.kt
```