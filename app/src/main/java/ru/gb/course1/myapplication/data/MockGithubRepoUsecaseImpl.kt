package ru.gb.course1.myapplication.data

import androidx.lifecycle.LiveData
import ru.gb.course1.myapplication.domain.GithubRepoEntity
import ru.gb.course1.myapplication.domain.GithubRepoUsecase

class MockGithubRepoUsecaseImpl : GithubRepoUsecase {
    override fun getReposForUserSync(userName: String): List<GithubRepoEntity> {
        return listOf(GithubRepoEntity(234L, "Name", "kotlin", "jkshdfbvjhsbdv"))
    }

    override fun getReposForUser(userName: String): LiveData<List<GithubRepoEntity>> {
        TODO("Not yet implemented")
    }

    override fun getReposForUserAsync(
        userName: String,
        onSuccess: (List<GithubRepoEntity>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Thread {
            Thread.sleep(5_000)
            onSuccess.invoke(getReposForUserSync(userName))
        }.start()
    }
}