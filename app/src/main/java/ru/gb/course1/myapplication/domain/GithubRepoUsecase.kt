package ru.gb.course1.myapplication.domain

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

interface GithubRepoUsecase {
    @WorkerThread
    @Throws(Throwable::class)
    fun getReposForUserSync(userName: String): List<GithubRepoEntity>

    fun getReposForUser(userName: String): LiveData<List<GithubRepoEntity>>

    fun getReposForUserAsync(
        userName: String,
        onSuccess: (List<GithubRepoEntity>) -> Unit,
        onError: (Throwable) -> Unit
    )

}