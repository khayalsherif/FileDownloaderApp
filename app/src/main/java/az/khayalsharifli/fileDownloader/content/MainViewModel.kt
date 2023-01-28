package az.khayalsharifli.fileDownloader.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.khayalsharifli.fileDownloader.data.NetworkRepository
import az.khayalsharifli.fileDownloader.data.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class MainViewModel(
    private val repository: NetworkRepository,
) : ViewModel() {

    private val _fileResponse =
        MutableStateFlow<NetworkResult<ResponseBody>>(NetworkResult.Empty())
    val fileResponse: StateFlow<NetworkResult<ResponseBody>> get() = _fileResponse

    fun downlandFile(fileUrl: String) = viewModelScope.launch(Dispatchers.IO) {
        _fileResponse.emit(NetworkResult.Loading())
        try {
            val response = repository.downlandFile(fileUrl)
            _fileResponse.emit(handleGetHistoryResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            _fileResponse.emit(NetworkResult.Error("The job was cancelled."))
        }
    }

    private fun handleGetHistoryResponse(response: Response<ResponseBody>): NetworkResult<ResponseBody> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body())
            }
            else -> {
                NetworkResult.Error(
                    "Something went wrong."
                )
            }
        }
    }

    fun saveFile(file: File, response: ResponseBody) {
        response.byteStream().use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}




