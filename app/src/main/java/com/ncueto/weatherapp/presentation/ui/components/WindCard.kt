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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
            .border(1.dp, White, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundBlue)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.wind_card_title),
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wind speed info
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
                                text = stringResource(R.string.kilometers_unit),
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.wind_card_title),
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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
                            text = "${((windGust ?: 0.0) * 3.6).roundToInt()}",
                            color = TextWhite,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.kilometers_unit),
                                color = TextWhiteSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.rachas_text),
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

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
            val innerCircleRadius = 22.dp.toPx()

            // Draw 12 tick marks
            for (i in 0 until 12) {
                val angle = i * 30f - 90f
                val rad = Math.toRadians(angle.toDouble())
                val tickInner = outerRadius - 7.dp.toPx()
                val tickOuter = outerRadius - 0.dp.toPx()

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
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw center circle
            drawCircle(
                color = White,
                radius = innerCircleRadius,
                center = center,
                style = Fill
            )
            drawCircle(
                color = Color(0x40BDBDBD),
                radius = innerCircleRadius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Draw orange dot for wind direction
            val windRad = Math.toRadians((windDegree - 90).toDouble())
            val dotDistance = innerCircleRadius + 8.dp.toPx()
            val dotPosition = Offset(
                center.x + (dotDistance * cos(windRad)).toFloat(),
                center.y + (dotDistance * sin(windRad)).toFloat()
            )
            drawCircle(
                color = Color(0xFFFF5722),
                radius = 5.dp.toPx(),
                center = dotPosition,
                style = Fill
            )
            drawCircle(
                color = Color(0xFFFFAB91),
                radius = 2.dp.toPx(),
                center = dotPosition,
                style = Fill
            )
        }

        // Cardinal directions
        Text(
            text = "N",
            color = Color(0xFFE53935),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )
        Text(
            text = "S",
            color = Color(0xFF757575),
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
        Text(
            text = "E",
            color = Color(0xFF757575),
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
        )
        Text(
            text = "W",
            color = Color(0xFF757575),
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        )

        // Degrees
        Box(
            modifier = Modifier.size(46.dp)
        ) {
            Text(
                text = "${windDegree}º",
                color = Color(0xFF5D4037),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = getWindDirection(windDegree),
                color = Color(0xFF5D4037),
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
fun WindCardPreview() {
    WindCard(
        windSpeed = 10.0,
        windGust = 5.0,
        windDegree = 45
    )
}

@Preview
@Composable
fun WindCompassPreview() {
    WindCompass(180)
}

private fun getWindDirection(degree: Int): String {
    return when {
        degree >= 337.5 || degree < 22.5 -> "n"
        degree >= 22.5 && degree < 67.5 -> "ne"
        degree >= 67.5 && degree < 112.5 -> "e"
        degree >= 112.5 && degree < 157.5 -> "se"
        degree >= 157.5 && degree < 202.5 -> "s"
        degree >= 202.5 && degree < 247.5 -> "sw"
        degree >= 247.5 && degree < 292.5 -> "w"
        degree >= 292.5 && degree < 337.5 -> "nw"
        else -> ""
    }
}