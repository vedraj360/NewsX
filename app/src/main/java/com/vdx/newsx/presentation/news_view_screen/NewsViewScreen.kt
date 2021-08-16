package com.vdx.newsx.presentation.news_view_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vdx.newsx.presentation.components.CircularIndeterminateProgressBar
import com.vdx.newsx.presentation.components.ComposeSnackbar
import com.vdx.newsx.presentation.components.TopBar
import com.vdx.newsx.presentation.news_feed.NewsFeedViewModel
import com.vdx.newsx.presentation.util.MainActions
import com.vdx.newsx.presentation.util.SnackbarController
import com.vdx.newsx.util.decode
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsViewScreen(
    navController: NavController,
    actions: MainActions,
    urlToRender: String?,
    viewModel: NewsFeedViewModel = hiltViewModel()
) {
    Timber.d("News Url ${urlToRender}")
    val loading = remember { mutableStateOf(false) }
    val snackbarVisibleState = remember { mutableStateOf(true) }
    var rememberWebViewProgress: Int by remember { mutableStateOf(-1) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = SnackbarController(coroutineScope)
    val isPresent = viewModel.isPresent.collectAsState()
    viewModel.isNewPresent(viewModel.result)
    Scaffold(
        topBar = {
            TopBar(
                title = "",
                if (isPresent.value) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                action = actions
            ) {
                viewModel.result?.let {
                    if (!isPresent.value) {
                        viewModel.saveNews(it)
                        snackbarController.getScope().launch {
                            snackbarController.showSnackbar(
                                scaffoldState = scaffoldState,
                                message = "Article saved successfully",
                                actionLabel = ""
                            )
                        }
                    } else {
                        viewModel.deleteNews(it)
                        snackbarController.getScope().launch {
                            snackbarController.showSnackbar(
                                scaffoldState = scaffoldState,
                                message = "Article deleted successfully",
                                actionLabel = ""
                            )
                        }
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        }
    ) {
        Box {
            CustomWebView(
                modifier = Modifier.fillMaxSize(),
                url = urlToRender.toString().decode(),
                onProgressChange = { progress ->
                    rememberWebViewProgress = progress
                },
                onCircularProgressChange = { progress ->
                    loading.value = progress
                },
                initSettings = { settings ->
                    settings?.apply {
                        javaScriptEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = true
                        javaScriptCanOpenWindowsAutomatically = true
                        cacheMode = WebSettings.LOAD_NO_CACHE
                    }
                }, onBack = { webView ->
                    if (webView?.canGoBack() == true) {
                        webView.goBack()
                    } else {
                        navController.navigateUp()
                    }
                }, onReceivedError = {

                }
            )
            /*
            ? Linear progress bar
            LinearProgressIndicator(
                progress = rememberWebViewProgress * 1.0F / 100F,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (rememberWebViewProgress == 100) 0.dp else 2.dp),
                color = Primary
            )*/
            CircularIndeterminateProgressBar(loading.value)
            ComposeSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

}


@Composable
fun CustomWebView(
    modifier: Modifier = Modifier,
    url: String,
    onBack: (webView: WebView?) -> Unit,
    onProgressChange: (progress: Int) -> Unit = {},
    onCircularProgressChange: (progress: Boolean) -> Unit = {},
    initSettings: (webSettings: WebSettings?) -> Unit = {},
    onReceivedError: (error: WebResourceError?) -> Unit = {}
) {
    val webViewChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    val webViewClient = object : WebViewClient() {
        override fun onPageStarted(
            view: WebView?, url: String?,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
            onProgressChange(-1)
            onCircularProgressChange(true)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onProgressChange(100)
            onCircularProgressChange(false)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (null == request?.url) return false
            val showOverrideUrl = request.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")
                ) {
                    Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            } catch (e: Exception) {
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            onReceivedError(error)
            onCircularProgressChange(false)

        }
    }
    var webView: WebView? = null
    val coroutineScope = rememberCoroutineScope()
    AndroidView(modifier = modifier, factory = { ctx ->
        WebView(ctx).apply {
            this.webViewClient = webViewClient
            this.webChromeClient = webViewChromeClient
            initSettings(this.settings)
            webView = this
            loadUrl(url)
        }
    })
    BackHandler {
        coroutineScope.launch {
            onBack(webView)
        }
    }
}
