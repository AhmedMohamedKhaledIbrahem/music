package com.example.music.data

import com.example.music.data.model.YouTubeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("search")
    fun searchVideos(
        @Query("part") part:String,
        @Query("q") query:String,
        @Query("key") apiKey:String,
        @Query("type") type:String,
        @Query("maxResults") maxResult:Int
    ): Observable<YouTubeModel>
}