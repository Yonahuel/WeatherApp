package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ncueto.weatherapp.R
import com.ncueto.weatherapp.presentation.ui.theme.BackgroundBlue
import com.ncueto.weatherapp.presentation.ui.theme.TextWhite
import com.ncueto.weatherapp.presentation.ui.theme.TextWhiteSecondary
import com.ncueto.weatherapp.presentation.ui.theme.White
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun WindCard(
    windSpeed: Double,
    windGust: Double?,
    windDegree: Int,
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
            // Header - "Viento" más grande
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind_indicator),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Viento",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wind speed info - números más grandes
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Viento row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${(windSpeed * 3.6).roundToInt()}",
                            color = TextWhite,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "km/h",
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Viento",
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Divider - blanco sólido, más largo
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        thickness = 1.dp,
                        color = White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rachas row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${((windGust ?: windSpeed) * 3.6).roundToInt()}",
                            color = TextWhite,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "km/h",
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Rachas",
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Compass con fondo blanco
                WindCompass(
                    windDegree = windDegree,
                    modifier = Modifier.size(110.dp)
                )
            }
        }
    }
}

@Composable
private fun WindCompass(
    windDegree: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(110.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.minDimension / 2 - 4.dp.toPx()

            // Draw 12 tick marks cerca del borde
            for (i in 0 until 12) {
                val angle = i * 30f - 90f
                val rad = Math.toRadians(angle.toDouble())
                val tickInner = outerRadius - 8.dp.toPx()
                val tickOuter = outerRadius - 2.dp.toPx()

                // Los puntos cardinales (0, 3, 6, 9) son rojos, el resto grises
                val isCardinal = i % 3 == 0
                val tickColor = if (isCardinal) Color(0xFFE53935) else Color(0xFFBDBDBD)

                drawLine(
                    color = tickColor,
                    start = Offset(
                        center.x + (tickInner * cos(rad)).toFloat(),
                        center.y + (tickInner * sin(rad)).toFloat()
                    ),
                    end = Offset(
                        center.x + (tickOuter * cos(rad)).toFloat(),
                        center.y + (tickOuter * sin(rad)).toFloat()
                    ),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Draw center circle - ROJO
            drawCircle(
                color = Color(0xFFFFCDD2), // Rosa claro para el centro
                radius = 28.dp.toPx(),
                center = center,
                style = Fill
            )

            // Draw orange dot for wind direction (en lugar de aguja)
            val windRad = Math.toRadians((windDegree - 90).toDouble())
            val dotDistance = 20.dp.toPx()
            val dotPosition = Offset(
                center.x + (dotDistance * cos(windRad)).toFloat(),
                center.y + (dotDistance * sin(windRad)).toFloat()
            )
            drawCircle(
                color = Color(0xFFFF5722), // Naranja
                radius = 5.dp.toPx(),
                center = dotPosition,
                style = Fill
            )
        }

        // Cardinal directions - N es roja
        Text(
            text = "N",
            color = Color(0xFFE53935), // Rojo
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )
        Text(
            text = "S",
            color = Color(0xFF757575),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
        Text(
            text = "E",
            color = Color(0xFF757575),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        )
        Text(
            text = "W",
            color = Color(0xFF757575),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )

        // Degree in center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${windDegree}º",
                color = Color(0xFF5D4037),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = getWindDirection(windDegree),
                color = Color(0xFF5D4037),
                fontSize = 10.sp
            )
        }
    }
}

private fun getWindDirection(degree: Int): String {
    return when {
        degree >= 337.5 || degree < 22.5 -> "N"
        degree >= 22.5 && degree < 67.5 -> "NE"
        degree >= 67.5 && degree < 112.5 -> "E"
        degree >= 112.5 && degree < 157.5 -> "SE"
        degree >= 157.5 && degree < 202.5 -> "S"
        degree >= 202.5 && degree < 247.5 -> "SW"
        degree >= 247.5 && degree < 292.5 -> "W"
        degree >= 292.5 && degree < 337.5 -> "NW"
        else -> ""
    }
}