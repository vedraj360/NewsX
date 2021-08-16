package com.vdx.newsx.presentation.news_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vdx.newsx.R
import com.vdx.newsx.presentation.components.NewsColumnItem
import com.vdx.newsx.presentation.components.NewsRowItem
import com.vdx.newsx.presentation.ui.theme.*
import com.vdx.newsx.presentation.util.MainActions
import com.vdx.newsx.presentation.util.shimmer.ShimmerAnimation
import com.vdx.newsx.util.Constants
import com.vdx.newsx.util.encode

@Composable
fun NewsScreen(
    navController: NavController,
    actions: MainActions,
    viewModel: NewsFeedViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        val featuredNews = viewModel.getFeatureNewsFeed.collectAsState()
        val news = viewModel.getNewsFeed.collectAsState()
        val listScrollState = rememberLazyListState()
        val bottomListScrollState = rememberLazyListState()
        val featureNewsLoading = viewModel.featureNewsLoading.value
        val newsLoading = viewModel.newsLoading.value

        Column(modifier = Modifier.background(CardBackground)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp),
                text = stringResource(R.string.newx),
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                maxLines = Constants.MAX_NEWS_DESCRIPTION_LINES,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            LazyRow(state = listScrollState) {
                if (featureNewsLoading) {
                    repeat(5) {
                        item {
                            ShimmerAnimation(
                                Modifier
                                    .width(320.dp)
                                    .height(240.dp)
                                    .clip(Shapes.medium)
                                    .padding(16.dp)
                            )
                        }
                    }
                } else {
                    if (featuredNews.value.results.isNotEmpty()) {
                        items(items = featuredNews.value.results.reversed(), itemContent = { news ->
                            NewsRowItem(news = news) {
                                viewModel.result = news
                                actions.gotoNewsViewScreen(news.url.encode())
                            }
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
        Text(
            modifier = Modifier.padding(start = 15.dp, top = 15.dp),
            text = stringResource(R.string.recommended),
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = Constants.MAX_NEWS_DESCRIPTION_LINES,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier
                .background(Background)
                .padding(bottom = SpaceLarge + 20.dp),
            state = bottomListScrollState
        ) {
            if (newsLoading) {
                repeat(5) {
                    item {
                        ShimmerAnimation(
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(Shapes.medium)
                                .padding(16.dp)
                        )
                    }
                }
            } else {
                if (featuredNews.value.results.isNotEmpty()) {
                    items(items = news.value.results, itemContent = { news ->
                        NewsColumnItem(news = news) {
                            viewModel.result = news
                            actions.gotoNewsViewScreen(news.url.encode())
                        }
                    })
                }
            }
        }
    }
}