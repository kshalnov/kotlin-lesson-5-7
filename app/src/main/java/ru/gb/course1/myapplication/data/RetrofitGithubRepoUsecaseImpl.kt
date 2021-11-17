package ru.gb.course1.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.course1.myapplication.data.retrofit.GithubRepoApi
import ru.gb.course1.myapplication.domain.GithubRepoEntity
import ru.gb.course1.myapplication.domain.GithubRepoUsecase


private const val BASE_URL = "https://api.github.com/"

class RetrofitGithubRepoUsecaseImpl : GithubRepoUsecase {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var api: GithubRepoApi = retrofit.create(GithubRepoApi::class.java)

    override fun getReposForUserSync(userName: String): List<GithubRepoEntity> {
        return api.loadReposByUser(userName).execute().body() ?: emptyList()
    }

    override fun getReposForUser(userName: String): LiveData<List<GithubRepoEntity>> {
        val liveData: MutableLiveData<List<GithubRepoEntity>> = MutableLiveData()

        api.loadReposByUser(userName).enqueue(object : Callback<List<GithubRepoEntity>> {
            override fun onResponse(
                call: Call<List<GithubRepoEntity>>,
                response: Response<List<GithubRepoEntity>>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    liveData.postValue(body)
                }
            }

            override fun onFailure(call: Call<List<GithubRepoEntity>>, t: Throwable) {
                // todo ???
            }

        })
        return liveData
    }

    override fun getReposForUserAsync(
        userName: String,
        onSuccess: (List<GithubRepoEntity>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.loadReposByUser(userName).enqueue(object : Callback<List<GithubRepoEntity>> {
            override fun onResponse(
                call: Call<List<GithubRepoEntity>>,
                response: Response<List<GithubRepoEntity>>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: throw IllegalStateException("null result"))
                } else {
                    onError(Throwable("unknown error"))
                }
            }

            override fun onFailure(call: Call<List<GithubRepoEntity>>, t: Throwable) {
                onError(t)
            }
        })
    }
}