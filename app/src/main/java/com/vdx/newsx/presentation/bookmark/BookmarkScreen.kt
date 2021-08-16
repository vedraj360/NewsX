package com.vdx.newsx.presentation.bookmark

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vdx.newsx.R
import com.vdx.newsx.presentation.components.ComposeSnackbar
import com.vdx.newsx.presentation.components.NewsColumnItem
import com.vdx.newsx.presentation.news_feed.NewsFeedViewModel
import com.vdx.newsx.presentation.ui.theme.Background
import com.vdx.newsx.presentation.ui.theme.SpaceLarge
import com.vdx.newsx.presentation.ui.theme.TextWhite
import com.vdx.newsx.presentation.util.MainActions
import com.vdx.newsx.presentation.util.SnackbarController
import com.vdx.newsx.util.Constants
import com.vdx.newsx.util.encode
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookmarkScreen(
    navController: NavController,
    actions: MainActions,
    viewModel: NewsFeedViewModel = hiltViewModel()
) {
    viewModel.getSavedNewsItems()
    val bottomListScrollState = rememberLazyListState()
    val newsList = viewModel.getSavedNewsFeed.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = SnackbarController(coroutineScope)
    Scaffold(snackbarHost = {
        scaffoldState.snackbarHostState
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp),
                text = stringResource(R.string.bookmark),
                style = MaterialTheme.typography.h4,
                overflow = TextOverflow.Ellipsis,
                maxLines = Constants.MAX_NEWS_DESCRIPTION_LINES,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .background(Background)
                        .padding(bottom = SpaceLarge + 20.dp),
                    state = bottomListScrollState
                ) {
                    if (newsList.value.isNotEmpty()) {
                        items(
                            items = newsList.value,
                            key = { news -> news.url },
                            itemContent = { news ->
                                var isDeleted by remember { mutableStateOf(false) }
                                val dismissState = rememberDismissState(
                                    confirmStateChange = {
                                        Timber.d("dismiss value ${it.name}")
                                        if (it == DismissValue.DismissedToEnd) isDeleted =
                                            !isDeleted
                                        else if (it == DismissValue.DismissedToStart) isDeleted =
                                            !isDeleted
                                        it != DismissValue.DismissedToStart || it != DismissValue.DismissedToEnd
                                    }
                                )
                                SwipeToDismiss(
                                    state = dismissState,
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                        DismissDirection.EndToStart
                                    ),
                                    dismissThresholds = { direction ->
                                        FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                                    },
                                    background = {
                                        val direction =
                                            dismissState.dismissDirection ?: return@SwipeToDismiss
                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                DismissValue.Default -> Color.LightGray
                                                DismissValue.DismissedToEnd -> Color.Red
                                                DismissValue.DismissedToStart -> Color.Red
                                            }
                                        )
                                        val alignment = when (direction) {
                                            DismissDirection.StartToEnd -> Alignment.CenterStart
                                            DismissDirection.EndToStart -> Alignment.CenterEnd
                                        }
                                        val icon = when (direction) {
                                            DismissDirection.StartToEnd -> Icons.Default.Delete
                                            DismissDirection.EndToStart -> Icons.Default.Delete
                                        }
                                        val scale by animateFloatAsState(
                                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                        )
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "Localized description",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }, dismissContent = {
                                        if (isDeleted) {
                                            viewModel.deleteNews(news)
                                            Timber.d("Deleted ${news.url}")
                                            snackbarController.getScope().launch {
                                                snackbarController.showSnackbar(
                                                    scaffoldState = scaffoldState,
                                                    message = "Article deleted successfully",
                                                    actionLabel = "Undo"
                                                )
                                                viewModel.result = news
                                            }
                                        } else {
                                            NewsColumnItem(news = news) {
                                                viewModel.result = news
                                                actions.gotoNewsViewScreen(news.url.encode())
                                            }
                                        }
                                    }
                                )
                            })

                    }
                }
                ComposeSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onDismiss = {
                        viewModel.result?.let {
                            viewModel.saveNews(it)
                        }
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = SpaceLarge + 20.dp)
                )
            }
        }
    }

}