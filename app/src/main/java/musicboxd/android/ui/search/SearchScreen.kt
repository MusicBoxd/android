package musicboxd.android.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import musicboxd.android.ui.common.ArtistHorizontalPreview
import musicboxd.android.ui.theme.MusicBoxdTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(searchScreenViewModel: SearchScreenViewModel = hiltViewModel()) {
    val searchArtistsResult =
        searchScreenViewModel.searchArtistsResult.collectAsStateWithLifecycle()
    val searchTracksResult = searchScreenViewModel.searchTracksResult.collectAsStateWithLifecycle()
    val searchAlbumsResult = searchScreenViewModel.searchAlbumsResult.collectAsStateWithLifecycle()
    val searchQuery = searchScreenViewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    MusicBoxdTheme {
        SearchBar(
            query = searchQuery.value,
            onQueryChange = {
                searchScreenViewModel.onUiEvent(SearchScreenUiEvent.OnQueryChange(it))
            },
            onSearch = {

            },
            active = isSearchActive.value,
            onActiveChange = {
                isSearchActive.value = it
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(searchArtistsResult.value.artists, key = { index, artist ->
                    artist.lastFMURL
                }) { index, artist ->
                    ArtistHorizontalPreview(
                        artistImgUrl = searchArtistsResult.value.artistImages[index],
                        artistName = artist.name
                    )
                }
            }
        }
    }
}