package ru.gb.course1.myapplication.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.gb.course1.myapplication.domain.GithubRepoEntity

interface GithubRepoApi {
    @GET("users/{user}/repos")
    fun loadReposByUser(@Path("user") userName: String): Call<List<GithubRepoEntity>>
}