package ru.gb.course1.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import ru.gb.course1.myapplication.app
import ru.gb.course1.myapplication.databinding.ActivityMainBinding
import ru.gb.course1.myapplication.domain.GithubRepoUsecase

private const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Analemma_fishburn.tif/lossy-page1-1280px-Analemma_fishburn.tif.jpg"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val githubRepoEntity: GithubRepoUsecase by lazy { app.githubRepoUsecase }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(IMAGE_URL)
            .into(binding.imageView)

        binding.downloadButton.setOnClickListener {
            showProgress(true)

            val userName = binding.userNameEditText.text.toString()

//            @@@ LiveData example @@@
//            githubRepoEntity.getReposForUser(userName).observe(this) {
//                val sb = StringBuilder()
//                it.forEach {
//                    sb.appendLine(it.toString())
//                }
//                binding.resultTextView.text = sb.toString()
//                showProgress(false)
//            }
            githubRepoEntity.getReposForUserAsync(
                userName,
                onSuccess = {
                    val sb = StringBuilder()
                    it.forEach {
                        sb.appendLine(it.toString())
                    }
                    runOnUiThread {
                        binding.resultTextView.text = sb.toString()
                        showProgress(false)
                    }
                },
                onError = {
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка ${it.message}", Toast.LENGTH_SHORT).show()
                        showProgress(false)
                    }
                }
            )
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible = show
        binding.resultTextView.isVisible = !show
        binding.downloadButton.isEnabled = !show
        binding.userNameEditText.isEnabled = !show
    }
}