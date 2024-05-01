package musicboxd.android.ui.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import musicboxd.android.ui.theme.MusicBoxdTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    val searchQuery = searchScreenViewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    MusicBoxdTheme {
        ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
            SearchBar(
                colors = SearchBarDefaults.colors(dividerColor = Color.Transparent),
                leadingIcon = {
                    IconButton(onClick = {
                        isSearchActive.value = false;searchScreenViewModel.onUiEvent(
                        SearchScreenUiEvent.OnQueryChange("")
                    )
                    }) {
                        Icon(
                            imageVector = if (isSearchActive.value) Icons.Default.ArrowBack else Icons.Default.Search,
                            contentDescription = if (isSearchActive.value) "Arrow Back Icon" else "Search Icon"
                        )
                    }
                },
                trailingIcon = {
                    if (isSearchActive.value) {
                        IconButton(onClick = {
                            if (searchQuery.value.isNotBlank()) searchScreenViewModel.onUiEvent(
                                SearchScreenUiEvent.OnQueryChange("")
                            ) else isSearchActive.value = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancel Icon"
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        text = "Search MusicBoxd",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isSearchActive.value) 0.dp else 15.dp)
                    .animateContentSize(),
                query = searchQuery.value,
                onQueryChange = {
                    searchScreenViewModel.onUiEvent(SearchScreenUiEvent.OnQueryChange(it))
                },
                onSearch = {

                },
                active = isSearchActive.value,
                onActiveChange = {
                    isSearchActive.value = it
                },
                content = { SearchContent(searchScreenViewModel = searchScreenViewModel) }
            )
        }
    }
}