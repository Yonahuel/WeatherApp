package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ncueto.weatherapp.presentation.ui.theme.CloudsRed
import com.ncueto.weatherapp.presentation.ui.theme.HumidityCyan
import com.ncueto.weatherapp.presentation.ui.theme.PressureYellow
import com.ncueto.weatherapp.presentation.ui.theme.TextWhite
import com.ncueto.weatherapp.presentation.ui.theme.White

@Composable
fun WeatherIndicatorsRow(
    pressure: Int,
    clouds: Int,
    humidity: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CircularIndicator(
            value = pressure.toString(),
            unit = "hPa",
            label = "Presión",
            progress = (pressure - 950f) / 100f,
            color = PressureYellow
        )
        CircularIndicator(
            value = "$clouds",
            unit = "%",
            label = "Nubes",
            progress = clouds / 100f,
            color = CloudsRed
        )
        CircularIndicator(
            value = "$humidity",
            unit = "%",
            label = "Humedad",
            progress = humidity / 100f,
            color = HumidityCyan
        )
    }
}

@Composable
private fun CircularIndicator(
    value: String,
    unit: String,
    label: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(70.dp)
        ) {
            // Background arc - blanco sólido (sin transparencia)
            Canvas(modifier = Modifier.size(70.dp)) {
                drawArc(
                    color = White,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            // Progress arc - color según indicador
            Canvas(modifier = Modifier.size(70.dp)) {
                drawArc(
                    color = color,
                    startAngle = 135f,
                    sweepAngle = 270f * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            // Value text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = value,
                        color = color,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unit.isNotEmpty()) {
                        Text(
                            text = unit,
                            color = color,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
        Text(
            text = label,
            color = TextWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}