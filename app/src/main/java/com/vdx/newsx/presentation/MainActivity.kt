package com.vdx.newsx.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.vdx.newsx.presentation.components.StandardScaffold
import com.vdx.newsx.presentation.news_feed.NewsFeedViewModel
import com.vdx.newsx.presentation.ui.theme.Background
import com.vdx.newsx.presentation.ui.theme.NewsXTheme
import com.vdx.newsx.presentation.util.Navigation
import com.vdx.newsx.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsXTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Background) {
                    val navController = rememberAnimatedNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val viewModel: NewsFeedViewModel = hiltViewModel()
                    StandardScaffold(
                        navController = navController,
                        showBottomBar = navBackStackEntry?.destination?.route in listOf(
                            Screen.NewsScreen.route,
                            Screen.Bookmark.route,
                        ),
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Navigation(navController, viewModel)
                    }
                }
            }
        }
    }
}
