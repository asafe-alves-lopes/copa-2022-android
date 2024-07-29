package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke() = matchesRepository.getMatches()
}