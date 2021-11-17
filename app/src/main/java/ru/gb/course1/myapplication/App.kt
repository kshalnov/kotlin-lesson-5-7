package ru.gb.course1.myapplication

import android.app.Application
import android.content.Context
import ru.gb.course1.myapplication.data.MockGithubRepoUsecaseImpl
import ru.gb.course1.myapplication.data.RetrofitGithubRepoUsecaseImpl
import ru.gb.course1.myapplication.data.WebGithubRepoUsecaseImpl
import ru.gb.course1.myapplication.domain.GithubRepoUsecase

class App : Application() {
    val githubRepoUsecase: GithubRepoUsecase by lazy { RetrofitGithubRepoUsecaseImpl() }
}

val Context.app
    get() = applicationContext as App