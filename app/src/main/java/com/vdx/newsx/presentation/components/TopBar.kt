package com.vdx.newsx.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.vdx.newsx.R
import com.vdx.newsx.presentation.ui.theme.StatusBarColor
import com.vdx.newsx.presentation.util.MainActions

@Composable
fun TopBar(
    title: String,
    bookmarkIcon: ImageVector = Icons.Outlined.BookmarkBorder,
    action: MainActions,
    onBookmarkClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StatusBarColor)
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.text_back_button),
            tint = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.clickable(onClick = action.upPress)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, style = typography.h5, color = MaterialTheme.colors.primaryVariant)
        Icon(
            imageVector = bookmarkIcon,
            tint = androidx.compose.ui.graphics.Color.White,
            contentDescription = stringResource(R.string.text_back_button),
            modifier = Modifier.clickable(onClick = onBookmarkClick),
        )
    }
}

@Preview
@Composable
fun ComposablePreview() {
    val navController = rememberNavController()
    TopBar("", action = MainActions(navController))
}