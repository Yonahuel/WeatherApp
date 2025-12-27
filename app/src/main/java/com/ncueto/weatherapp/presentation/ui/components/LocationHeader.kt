package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ncueto.weatherapp.R
import com.ncueto.weatherapp.presentation.ui.theme.White
import com.ncueto.weatherapp.presentation.ui.theme.WhiteTransparent40

@Composable
fun LocationHeader(
    locationName: String,
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Settings icon - sin fondo
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Configuración",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Location chip - fondo blanco semi-transparente
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(WhiteTransparent40)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = locationName,
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Buscar",
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Share icon - sin fondo
        IconButton(
            onClick = onShareClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "Compartir",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}