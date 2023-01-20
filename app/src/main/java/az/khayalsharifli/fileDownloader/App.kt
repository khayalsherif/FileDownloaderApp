package az.khayalsharifli.fileDownloader

import android.app.Application
import az.khayalsharifli.fileDownloader.di.module
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(module))
        }
    }
}