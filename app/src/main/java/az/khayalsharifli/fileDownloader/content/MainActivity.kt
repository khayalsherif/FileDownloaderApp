package az.khayalsharifli.fileDownloader.content

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import az.khayalsharifli.fileDownloader.data.NetworkResult
import az.khayalsharifli.fileDownloader.databinding.ActivityMainBinding
import az.khayalsharifli.fileDownloader.tools.gone
import az.khayalsharifli.fileDownloader.tools.isEnable
import az.khayalsharifli.fileDownloader.tools.isNotEnable
import az.khayalsharifli.fileDownloader.tools.visible
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeResponse()
        binding.buttonStart.setOnClickListener {
            job = viewModel.downlandFile(binding.editTextUrl.text.toString())
            job.start()
            it.isNotEnable()
        }

        binding.buttonCancel.setOnClickListener {
            job.cancel()
            it.isNotEnable()
        }
    }

    private fun observeResponse() {
        lifecycleScope.launch {
            viewModel.fileResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        activeAllButton()
                        showMessage("Success")
                        binding.progresView.gone()
                        viewModel.saveFile(file = file(), it.data!!)
                    }
                    is NetworkResult.Error -> {
                        activeAllButton()
                        binding.progresView.gone()
                        showMessage(it.message.toString())
                    }
                    is NetworkResult.Loading -> {
                        binding.progresView.visible()
                        binding.buttonCancel.isEnable()
                    }
                    is NetworkResult.Empty -> {
                        binding.progresView.gone()
                    }
                }
            }
        }
    }

    private fun activeAllButton() {
        binding.buttonStart.isEnable()
        binding.buttonCancel.isEnable()
    }

    private fun file(): File {
        val dirPath: String = getExternalFilesDir(null)!!.absolutePath
        val filePath = "$dirPath/pdf"
        return File(filePath)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG)
            .show()
    }
}