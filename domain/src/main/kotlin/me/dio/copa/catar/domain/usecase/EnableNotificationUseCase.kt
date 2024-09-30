package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.notification.NotificationScheduler
import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class EnableNotificationUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository,
    private val notificationScheduler: NotificationScheduler,
) {
    suspend operator fun invoke(match: Match, title: String, content: String) {
        notificationScheduler.create(match, title, content)
        matchesRepository.enableNotificationFor(match.id)
    }
}