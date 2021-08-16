package com.vdx.newsx.presentation.util

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.vdx.newsx.presentation.bookmark.BookmarkScreen
import com.vdx.newsx.presentation.news_feed.NewsFeedViewModel
import com.vdx.newsx.presentation.news_feed.NewsScreen
import com.vdx.newsx.presentation.news_view_screen.NewsViewScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController, viewModel: NewsFeedViewModel) {
    val actions = remember(navController) { MainActions(navController) }
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.NewsScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            Screen.NewsScreen.route,
            exitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            NewsScreen(navController, actions, viewModel = viewModel)
        }
        composable(Screen.NewsViewScreen.route + "/{url}",
            arguments = listOf(navArgument("url") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }),
            exitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }) { entry ->
            NewsViewScreen(
                navController = navController,
                actions = actions,
                urlToRender = entry.arguments?.getString("url"),
                viewModel = viewModel
            )
        }
        composable(Screen.Bookmark.route, exitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }, popEnterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { 300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        }) {
            BookmarkScreen(
                navController = navController,
                actions = actions,
                viewModel = viewModel,
            )
        }
    }
}

class MainActions(navController: NavController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

    val gotoNewsViewScreen: (String) -> Unit = { url ->
        navController.navigate("${Screen.NewsViewScreen.route}/$url")
    }

}