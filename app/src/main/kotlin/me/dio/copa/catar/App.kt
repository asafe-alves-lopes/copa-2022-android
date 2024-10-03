package me.dio.copa.catar

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.hilt.android.HiltAndroidApp
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.notification.scheduler.extensions.NotificationSchedulerWorker
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var myWorkerFactory: MyWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(myWorkerFactory)
            .build()
}

class MyWorkerFactory @Inject constructor(private val matchesRepository: MatchesRepository) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return NotificationSchedulerWorker(matchesRepository, appContext, workerParameters)
    }
}

