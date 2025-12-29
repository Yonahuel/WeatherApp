package com.ncueto.weatherapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
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
    onShareClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLocationClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Settings icon
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(R.string.settings_icon_content_description),
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Location chip
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(WhiteTransparent40)
                .clickable { onSearchClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Location icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = stringResource(R.string.location_icon_content_description),
                    tint = White,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            onLocationClick()
                        }
                )
                Text(
                    text = locationName,
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                // Search icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search_icon_content_description),
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Share icon
        IconButton(
            onClick = onShareClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = stringResource(R.string.share_icon_content_description),
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}