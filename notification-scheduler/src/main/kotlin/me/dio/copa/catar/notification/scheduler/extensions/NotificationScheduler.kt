package me.dio.copa.catar.notification.scheduler.extensions

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.notification.NotificationScheduler
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

private const val NotificationSchedulerConstantsTitle = "NotificationSchedulerConstantsTitle"
private const val NotificationSchedulerConstantsContent = "NotificationSchedulerConstantsContent"

class NotificationSchedulerWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {

        val notificationTitle = inputData.getString(NotificationSchedulerConstantsTitle) ?: "title"
        val notificationContent =
            inputData.getString(NotificationSchedulerConstantsContent) ?: "content"

        context.showNotification(notificationTitle, notificationContent)

        return Result.success()
    }
}

class NotificationSchedulerImpl @Inject constructor(private val context: Context) :
    NotificationScheduler {

    override fun create(match: Match, title: String, content: String) {
        val notificationData = Data.Builder()
            .putString(NotificationSchedulerConstantsTitle, title)
            .putString(NotificationSchedulerConstantsContent, content)
            .build()

        val timeUntilTheNotification =
            Duration.between(LocalDateTime.now(), match.date).minusMinutes(15)

        val notificationWork = OneTimeWorkRequestBuilder<NotificationSchedulerWorker>()
            .setInitialDelay(timeUntilTheNotification)
            .setInputData(notificationData)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(match.id, ExistingWorkPolicy.REPLACE, notificationWork)
    }

    override fun delete(matchId: String) {
        WorkManager.getInstance(context).cancelUniqueWork(matchId)
    }
}