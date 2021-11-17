package ru.gb.course1.myapplication.data

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import ru.gb.course1.myapplication.domain.GithubRepoEntity
import ru.gb.course1.myapplication.domain.GithubRepoUsecase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WebGithubRepoUsecaseImpl : GithubRepoUsecase {

    override fun getReposForUserSync(userName: String): List<GithubRepoEntity> {
        val result = emptyList<GithubRepoEntity>().toMutableList()

        var connection: HttpURLConnection? = null
        try {
            connection = getUrl(userName).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5_000

            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            val resultJsonString = bufferedReader.readLines().toString()

            val reposArray =
                gson.fromJson(resultJsonString, Array<Array<GithubRepoEntity>>::class.java)

            reposArray.forEach { array ->
                array.forEach {
                    result.add(it)
                }
            }
        } catch (thr: Throwable) {
            connection?.disconnect()
            throw thr
        } finally {
            connection?.disconnect()
        }

        return result
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
            try {
                val res = getReposForUserSync(userName)
                onSuccess.invoke(res)
            } catch (thr: Throwable) {
                onError(thr)
            }
        }.start()
    }

    private val gson by lazy { Gson() }

    private fun getUrl(userName: String) = URL("https://api.github.com/users/$userName/repos")

}