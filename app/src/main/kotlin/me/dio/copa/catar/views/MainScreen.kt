package me.dio.copa.catar.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.extensions.getDate
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.ui.theme.Sizes


@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val state = viewModel.state.collectAsState()
    when (state.value) {
        MainState.Empty -> EmptyScreen(stringResource(id = R.string.msg_no_matches))
        is MainState.Error -> ErrorScreen(error = (state.value as MainState.Error).error)
        MainState.Loading -> LoadingScreen(stringResource(id = R.string.msg_loading_matches))
        is MainState.Success -> SuccessScreen(matches = (state.value as MainState.Success).matches)
    }
}

@Composable
fun EmptyScreen(
    message: String,
) {
    Message(message = message)
}

@Composable
fun LoadingScreen(
    message: String,
) {
    Message(message = message) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .height(Sizes.defaultSpacing)
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ErrorScreen(
    error: MainErrors,
) {
    when (error) {
        MainErrors.LoadMatches -> Message(message = stringResource(id = R.string.msg_failed_load_matches))
    }
}

@Composable
fun Message(
    message: String,
    moreContent: (@Composable () -> Unit)? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Text(
                textAlign = TextAlign.Center,
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
            )
            moreContent?.invoke()
        }
    }
}

@Composable
fun SuccessScreen(
    matches: List<Match>,
) {
    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Sizes.defaultSpacing),
        ) {
            items(matches.size) { index ->
                if (index == 0)
                    Spacer(modifier = Modifier.height(Sizes.defaultSpacing))
                MatchCard(match = matches[index])
            }
        }
    }
}

@Composable
fun MatchCard(
    match: Match,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Sizes.defaultSpacing,
                end = Sizes.defaultSpacing,
                bottom = Sizes.defaultSpacing
            )
            .background(Color.White)
            .shadow(Sizes.defaultShadow, MaterialTheme.shapes.medium, clip = false),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            CenterAlignedText(
                modifier = Modifier
                    .padding(Sizes.defaultSpacing),
                textToShow = "${match.name} - ${match.stadium.name}",
            )
            AsyncImage(
                model = match.stadium.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(160.dp),
            )
            Column(
                modifier = Modifier
                    .padding(Sizes.defaultSpacing),
            ) {
                CenterAlignedText(textToShow = "${match.team1.flag} ${match.team1.displayName} x ${match.team2.displayName} ${match.team2.flag}")
                CenterAlignedText(textToShow = "${stringResource(id = R.string.label_date)}: ${match.date.getDate()}")
            }
        }
    }
}

@Composable
fun CenterAlignedText(
    modifier: Modifier = Modifier,
    textToShow: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        textAlign = TextAlign.Center,
        text = textToShow
    )
}