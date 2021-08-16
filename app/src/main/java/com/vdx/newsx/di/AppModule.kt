package com.vdx.newsx.di

import android.content.Context
import androidx.room.Room
import com.vdx.newsx.BuildConfig
import com.vdx.newsx.data.local.NewsDB
import com.vdx.newsx.data.remote.NewsService
import com.vdx.newsx.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideBaseUrl() = Constants.SERVER_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsService =
        retrofit.create(NewsService::class.java)

    @Singleton
    @Provides
    fun provideNewsDb(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NewsDB::class.java, "news.db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideNewsDao(newsDB: NewsDB) = newsDB.newsDao()

}