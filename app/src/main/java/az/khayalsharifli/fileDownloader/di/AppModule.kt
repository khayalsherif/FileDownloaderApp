package az.khayalsharifli.dowlandfile.di

import az.khayalsharifli.dowlandfile.content.MainViewModel
import az.khayalsharifli.dowlandfile.data.NetworkRepository
import az.khayalsharifli.dowlandfile.data.NetworkService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val module = module {

    single {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)

        val logger =
            HttpLoggingInterceptor().setLevel(level = HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(logger)

        client.build()
    }

    factory<NetworkService> { get<Retrofit>().create(NetworkService::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(get())
            .build()
    }

    factory { NetworkRepository(get()) }

    viewModel { MainViewModel(get()) }
}