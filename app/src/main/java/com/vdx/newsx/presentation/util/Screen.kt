package com.vdx.newsx.presentation.util

sealed class Screen(val route: String) {
    object NewsScreen : Screen("news_screen")
    object NewsViewScreen : Screen("news_view_screen")
    object NewsSearch : Screen("news_search_screen")
    object Bookmark : Screen("bookmark_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
