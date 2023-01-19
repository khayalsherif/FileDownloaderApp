package az.khayalsharifli.dowlandfile

import android.app.Application
import az.khayalsharifli.dowlandfile.di.module
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(module))
        }
    }
}