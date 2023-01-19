package az.khayalsharifli.dowlandfile.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.khayalsharifli.dowlandfile.data.NetworkRepository
import az.khayalsharifli.dowlandfile.data.NetworkResult
import kotlinx.coroutines.Job
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

    fun downlandFile(fileUrl: String) = viewModelScope.launch {
        _fileResponse.emit(NetworkResult.Loading())
        try {
            val response = repository.downlandFile(fileUrl)
            _fileResponse.emit(handleGetHistoryResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            _fileResponse.emit(NetworkResult.Error("Something went wrong."))
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
}




