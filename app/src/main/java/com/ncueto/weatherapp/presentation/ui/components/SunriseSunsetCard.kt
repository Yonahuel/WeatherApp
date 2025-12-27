package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ncueto.weatherapp.R
import com.ncueto.weatherapp.presentation.ui.theme.BackgroundBlue
import com.ncueto.weatherapp.presentation.ui.theme.TextWhite
import com.ncueto.weatherapp.presentation.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun SunriseSunsetCard(
    sunrise: Long,
    sunset: Long,
    timezone: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundBlue)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header - icono blanco más grande, texto más grande
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_twilight),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Puesta de sol",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sunrise/Sunset icons with labels on top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Anochece (sunset) - izquierda
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Anochece ${formatTime(sunset, timezone)}",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sunset_landscape),
                        contentDescription = "Atardecer",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Amanece (sunrise) - derecha
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Amanece ${formatTime(sunrise, timezone)}",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sunrise_cloud),
                        contentDescription = "Amanecer",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long, timezoneOffset: Int): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("H:mm", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("GMT").apply {
        rawOffset = timezoneOffset * 1000
    }
    return format.format(date)
}