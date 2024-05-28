package musicboxd.android.ui.review

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.search.SearchContent
import musicboxd.android.ui.search.SearchScreenUiEvent
import musicboxd.android.ui.search.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    detailsViewModel: DetailsViewModel,
    searchScreenViewModel: SearchScreenViewModel,
    reviewScreenViewModel: ReviewScreenViewModel
) {
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        reviewScreenViewModel.loadLocalReviews()
    }
    val searchQuery = searchScreenViewModel.searchQuery.collectAsState()
    val localReviews = reviewScreenViewModel.localReviews.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
            SearchBar(colors = SearchBarDefaults.colors(dividerColor = Color.Transparent),
                leadingIcon = {
                    IconButton(onClick = {
                        isSearchActive.value = false;
                        searchScreenViewModel.onUiEvent(
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
                        text = "Search MusicBoxd", style = MaterialTheme.typography.titleSmall
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
                content = {
                    SearchContent(searchScreenViewModel = searchScreenViewModel,
                        navController = navController,
                        detailsViewModel = detailsViewModel,
                        inSearchScreen = false,
                        onSelectingAnItem = {
                            navController.navigate(NavigationRoutes.CREATE_A_NEW_REVIEW.name)
                        })
                })
        }
    }) {

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Text(
                    text = "Search for albums, tracks, or artists and share your reviews!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                Text(
                    text = "Or...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                FilledTonalButton(
                    onClick = { navController.navigate(NavigationRoutes.CREATE_A_NEW_LIST.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp)
                ) {
                    Text(
                        text = "Create a New List",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Drafts",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(localReviews.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoilImage(
                        imgUrl = it.releaseImgUrl,
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Column {
                        Text(
                            text = it.reviewTitle,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            fontWeight = FontWeight.Black,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = it.reviewContent,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            color = LocalContentColor.current.copy(0.8f)
                        )
                    }
                }
            }
        }
    }
}


