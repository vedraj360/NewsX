package com.vdx.newsx.data.repository

import com.vdx.newsx.domain.models.NewsModel
import com.vdx.newsx.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface INewsFeedRepository {
    suspend fun getFeatureNewsFeed(): Flow<NewsModel>
    suspend fun getNewsFeed(): Flow<NewsModel>
    suspend fun getAllSavedNews(): Flow<List<Result>>
    suspend fun saveNews(news: Result)
    suspend fun deleteNews(news: Result)
    suspend fun getNewsFeedById(id: String): Result?

}