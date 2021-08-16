package com.vdx.newsx.data.remote

import com.vdx.newsx.domain.models.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("svc/topstories/v2/home.json")
    suspend fun featureNewsFeed(@Query("api-key") apiKey: String): Response<NewsModel>

    @GET("svc/topstories/v2/world.json")
    suspend fun newsFeed(@Query("api-key") apiKey: String): Response<NewsModel>
}