package az.khayalsharifli.dowlandfile.content

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import az.khayalsharifli.dowlandfile.R
import az.khayalsharifli.dowlandfile.data.NetworkResult
import az.khayalsharifli.dowlandfile.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
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
        }

        binding.buttonCancel.setOnClickListener {
            job.cancel()
        }
    }

    private fun observeResponse() {
        lifecycleScope.launch {
            viewModel.fileResponse.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        binding.progresView.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
                    }
                    is NetworkResult.Error -> {
                        binding.progresView.visibility = View.GONE
                        Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is NetworkResult.Empty -> {
                        binding.progresView.visibility = View.GONE
                    }
                    is NetworkResult.Loading -> {
                        binding.progresView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}