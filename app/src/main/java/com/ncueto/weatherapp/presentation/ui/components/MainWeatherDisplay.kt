package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ncueto.weatherapp.R
import com.ncueto.weatherapp.presentation.ui.theme.TextWhite
import com.ncueto.weatherapp.presentation.ui.theme.TextWhiteSecondary
import com.ncueto.weatherapp.presentation.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@Composable
fun MainWeatherDisplay(
    temperature: Double,
    feelsLike: Double,
    description: String,
    tempMin: Double,
    tempMax: Double,
    windSpeed: Double,
    rain: Double?,
    timestamp: Long,
    timezone: Int,
    icon: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date and time
        Text(
            text = formatDateTime(timestamp, timezone),
            color = TextWhiteSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main temperature row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Wind and Rain (iconos de 24dp)
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = null,
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${(windSpeed * 3.6).roundToInt()} km/h",
                        color = TextWhiteSecondary,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rain),
                        contentDescription = null,
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${rain?.let { String.format("%.1f", it) } ?: "0"}mm",
                        color = TextWhiteSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            // Center - Weather icon and temperature
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = getWeatherIcon(icon)),
                    contentDescription = description,
                    tint = White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal)) {
                            append("${temperature.roundToInt()}")
                        }
                        withStyle(style = SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)) {
                            append("º")
                        }
                    },
                    color = White
                )
            }

            // Right side - Min/Max temp (iconos de 24dp)
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "Máxima",
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${tempMax.roundToInt()}º",
                        color = TextWhiteSecondary,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Mínima",
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${tempMin.roundToInt()}º",
                        color = TextWhiteSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Feels like
        Text(
            text = "Sensación de ${feelsLike.roundToInt()}º",
            color = TextWhiteSecondary,
            fontSize = 14.sp
        )

        // Description
        Text(
            text = description.replaceFirstChar { it.uppercase() },
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatDateTime(timestamp: Long, timezoneOffset: Int): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("d 'de' MMMM | HH:mm", Locale("es", "ES"))
    format.timeZone = TimeZone.getTimeZone("GMT").apply {
        rawOffset = timezoneOffset * 1000
    }
    return format.format(date)
}

private fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.ic_sunny
        "01n" -> R.drawable.ic_clear_night
        "02d", "02n" -> R.drawable.ic_partly_cloudy
        "03d", "03n" -> R.drawable.ic_cloudy
        "04d", "04n" -> R.drawable.ic_cloudy
        "09d", "09n" -> R.drawable.ic_rainy
        "10d", "10n" -> R.drawable.ic_rainy
        "11d", "11n" -> R.drawable.ic_thunderstorm
        "13d", "13n" -> R.drawable.ic_snowy
        "50d", "50n" -> R.drawable.ic_foggy
        else -> R.drawable.ic_sunny
    }
}