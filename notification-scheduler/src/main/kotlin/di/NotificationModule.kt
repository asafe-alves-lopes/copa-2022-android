package di

import dagger.Binds
import dagger.Module
import me.dio.copa.catar.domain.notification.NotificationScheduler
import me.dio.copa.catar.notification.scheduler.extensions.NotificationSchedulerImpl

@Module
interface NotificationModule {
    @Binds
    fun providesNotificationScheduler(impl: NotificationSchedulerImpl): NotificationScheduler
}