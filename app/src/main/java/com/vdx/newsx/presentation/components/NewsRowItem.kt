package com.vdx.newsx.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.vdx.newsx.domain.models.Result
import com.vdx.newsx.presentation.ui.theme.*
import com.vdx.newsx.util.Constants
import com.vdx.newsx.util.convertTime

@Composable
fun NewsRowItem(news: Result, openNewsWebView: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier =
        Modifier
            .width(320.dp)
            .padding(SpaceMedium)
            .clickable(onClick = openNewsWebView)
    ) {
        Column(
        ) {
            val imageUrl = if (news.multimedia.isNotEmpty()) {
                news.multimedia[0].url
            } else {
                ""
            }
            Image(
                rememberImagePainter(imageUrl, builder = {
                    crossfade(true)
                }),
                contentDescription = "News image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(Shapes.large),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = Constants.MAX_NEWS_DESCRIPTION_LINES,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = news.publishedDate.convertTime(context),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun NewsColumnItem(news: Result, openNewsWebView: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .padding(start = SpaceLarge, end = SpaceLarge, bottom = 10.dp, top = SpaceSmall)
            .clickable(onClick = openNewsWebView)
    ) {
        Row(modifier = Modifier.background(CardBackground)) {
            val imageUrl = if (news.multimedia.isNotEmpty()) {
                news.multimedia[0].url
            } else {
                ""
            }
            Image(
                rememberImagePainter(imageUrl, builder = {
                    crossfade(true)
                }),
                contentDescription = "News image",
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .padding(7.dp)
                    .clip(Shapes.large),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .padding(start = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (news.abstract.isEmpty()) news.title else news.abstract,
                    style = MaterialTheme.typography.subtitle1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = Constants.MAX_NEWS_DESCRIPTION_LINES,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = news.publishedDate.convertTime(context),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    color = TextGray
                )
            }
        }
    }
}