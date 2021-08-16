package com.vdx.newsx.data.local

import androidx.room.*
import com.vdx.newsx.domain.models.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * from news_list")
    fun getAll(): Flow<List<Result>>

    @Query("SELECT * from news_list where url = :url")
    fun getById(url: String): Result?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Result)

    @Update
    suspend fun update(item: Result)

    @Query("DELETE from news_list where url = :url")
    suspend fun delete(url: String)

    @Query("DELETE FROM news_list")
    suspend fun deleteAllNews()

}