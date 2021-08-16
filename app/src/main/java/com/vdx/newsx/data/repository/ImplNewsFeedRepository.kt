package com.vdx.newsx.data.repository

import com.vdx.newsx.data.local.NewsDao
import com.vdx.newsx.data.remote.NewsService
import com.vdx.newsx.domain.models.NewsModel
import com.vdx.newsx.domain.models.Result
import com.vdx.newsx.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ImplNewsFeedRepository @Inject constructor(
    val newsService: NewsService,
    val newsDao: NewsDao
) :
    INewsFeedRepository {
    override suspend fun getFeatureNewsFeed(): Flow<NewsModel> = flow {
        val response = newsService.featureNewsFeed(Constants.API_KEY)
        if (response.isSuccessful) {
            if (response.body() != null) {
                response.body()?.let { emit(it) }
            } else {
                emit(NewsModel())
            }
        }
    }.catch { e ->
        emit(NewsModel())
    }.flowOn(Dispatchers.IO)

    override suspend fun getNewsFeed(): Flow<NewsModel> = flow {
        val response = newsService.newsFeed(Constants.API_KEY)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            emit(NewsModel())
        }
    }.catch { e ->
        emit(NewsModel())
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllSavedNews(): Flow<List<Result>> = flow {
        emitAll(newsDao.getAll())
    }.catch { e ->
        emitAll(emptyFlow())
    }.flowOn(Dispatchers.IO)

    override suspend fun saveNews(news: Result) {
        newsDao.insert(news)
    }

    override suspend fun deleteNews(news: Result) {
        newsDao.delete(news.url)
    }

    override suspend fun getNewsFeedById(id: String): Result? {
        return newsDao.getById(id)
    }
}