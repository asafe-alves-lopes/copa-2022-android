package me.dio.copa.catar.domain.notification

import me.dio.copa.catar.domain.model.Match

interface NotificationScheduler {
    fun create(match: Match, title: String, content: String)
    fun delete(matchId: String)
}