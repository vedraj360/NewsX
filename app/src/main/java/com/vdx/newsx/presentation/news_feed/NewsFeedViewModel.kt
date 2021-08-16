package com.vdx.newsx.presentation.news_feed

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdx.newsx.data.repository.ImplNewsFeedRepository
import com.vdx.newsx.domain.models.NewsModel
import com.vdx.newsx.domain.models.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(private val newsFeedRepository: ImplNewsFeedRepository) :
    ViewModel() {

    private val _getFeatureNewsFeed = MutableStateFlow(NewsModel())
    val getFeatureNewsFeed: StateFlow<NewsModel> = _getFeatureNewsFeed

    private val _getNewsFeed = MutableStateFlow(NewsModel())
    val getNewsFeed: StateFlow<NewsModel> = _getNewsFeed

    val featureNewsLoading = mutableStateOf(false)
    val newsLoading = mutableStateOf(false)

    private val _getSavedNewsFeed =
        MutableStateFlow<List<Result>>(emptyList())
    val getSavedNewsFeed: StateFlow<List<Result>> = _getSavedNewsFeed

    private val _isPresent = MutableStateFlow(false)
    val isPresent: StateFlow<Boolean> = _isPresent

    var result: Result? = null

    init {
        viewModelScope.launch {
            featureNewsLoading.value = true
            newsLoading.value = true
            newsFeedRepository.getFeatureNewsFeed().collect { news ->
                _getFeatureNewsFeed.value = news
                featureNewsLoading.value = false
            }
            newsFeedRepository.getNewsFeed().collect { news ->
                _getNewsFeed.value = news
                newsLoading.value = false
            }
        }
    }

    fun getSavedNewsItems() {
        viewModelScope.launch {
            newsFeedRepository.getAllSavedNews().collect { news ->
                _getSavedNewsFeed.value = news
            }
        }
    }

    fun saveNews(news: Result) {
        viewModelScope.launch(Dispatchers.IO) {
            newsFeedRepository.saveNews(news)
            _isPresent.value = true
        }
    }


    fun deleteNews(news: Result) {
        viewModelScope.launch(Dispatchers.IO) {
            newsFeedRepository.deleteNews(news)
            _isPresent.value = false
        }
    }

    fun isNewPresent(news: Result?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = newsFeedRepository.getNewsFeedById(news?.url.toString())
            _isPresent.value = result != null
        }
    }
}