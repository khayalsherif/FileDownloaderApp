package az.khayalsharifli.dowlandfile.data

import okhttp3.ResponseBody
import retrofit2.Response

class NetworkRepository(
    private val serviceNetwork: NetworkService) {

    suspend fun downlandFile(fileUrl: String): Response<ResponseBody> {
        return serviceNetwork.downloadFile(fileUrl)
    }
}