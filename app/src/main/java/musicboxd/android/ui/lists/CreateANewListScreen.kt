package musicboxd.android.ui.lists

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.review.BooleanPreferenceGroup
import musicboxd.android.ui.search.SearchContent
import musicboxd.android.ui.search.SearchScreenUiEvent
import musicboxd.android.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateANewListScreen(
    detailsViewModel: DetailsViewModel,
    navController: NavController,
    createANewListScreenViewModel: CreateANewListScreenViewModel
) {
    val newListTile = rememberSaveable {
        mutableStateOf("")
    }
    val newListTitleInteractionSource = remember {
        MutableInteractionSource()
    }
    val newListDescription = rememberSaveable {
        mutableStateOf("")
    }
    val newListDescriptionInteractionSource = remember {
        MutableInteractionSource()
    }
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    val searchQuery = detailsViewModel.searchQuery.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(Modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = "Create a new List",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }, topBar = {
        if (isSearchActive.value) {
            ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
                SearchBar(colors = SearchBarDefaults.colors(dividerColor = Color.Transparent),
                    leadingIcon = {
                        IconButton(onClick = {
                            isSearchActive.value = false;
                            detailsViewModel.onUiEvent(
                                SearchScreenUiEvent.OnQueryChange("")
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Arrow Back Icon"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.value.isNotBlank()) detailsViewModel.onUiEvent(
                                SearchScreenUiEvent.OnQueryChange("")
                            ) else isSearchActive.value = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancel Icon"
                            )
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
                        detailsViewModel.onUiEvent(SearchScreenUiEvent.OnQueryChange(it))
                    },
                    onSearch = {

                    },
                    active = isSearchActive.value,
                    onActiveChange = {
                        isSearchActive.value = it
                    },
                    content = {
                        SearchContent(searchScreenViewModel = detailsViewModel,
                            navController = navController,
                            detailsViewModel = detailsViewModel,
                            inSearchScreen = false,
                            onSelectingAnItem = {
                                if (!createANewListScreenViewModel.currentSelection.value.map { it.itemUri }
                                        .contains(detailsViewModel.albumScreenState.itemUri)
                                ) {
                                    createANewListScreenViewModel.currentSelection.value += detailsViewModel.albumScreenState
                                }
                                isSearchActive.value = false
                                coroutineScope.launch {
                                    lazyGridState.animateScrollToItem(lazyGridState.layoutInfo.totalItemsCount - 1)
                                }
                            })
                    })
            }
        } else {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            }, title = {
                Text(
                    text = "Create a new List",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    }) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), columns = GridCells.Fixed(3),
            state = lazyGridState
        ) {
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Text(
                    text = "Name of the List",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                BasicTextField(
                    interactionSource = newListTitleInteractionSource,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    value = newListTile.value,
                    onValueChange = { newValue ->
                        newListTile.value = newValue
                    },
                    textStyle = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = LocalContentColor.current
                    )
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                    color = if (newListTitleInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                        0.65f
                    )
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Text(
                    text = "Description of the List",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                BasicTextField(
                    interactionSource = newListDescriptionInteractionSource,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 75.dp)
                        .padding(start = 15.dp, end = 15.dp),
                    value = newListDescription.value,
                    onValueChange = { newValue ->
                        newListDescription.value = newValue
                    },
                    textStyle = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = LocalContentColor.current
                    )
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                    color = if (newListDescriptionInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                        0.65f
                    )
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Save as a Public List",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(checked = true, onCheckedChange = {})
                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Text(
                    text = "Is this a numbered List?",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                BooleanPreferenceGroup {

                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Music Content",
                        style = MaterialTheme.typography.titleMedium
                    )
                    FilledTonalIconButton(onClick = { navController.navigate(NavigationRoutes.REORDER_MUSIC_CONTENT_SCREEN.name) }) {
                        Icon(imageVector = Icons.Default.EditNote, contentDescription = "")
                    }
                }
            }
            itemsIndexed(
                createANewListScreenViewModel.currentSelection.value,
                key = { index, it -> it.itemUri }) { index, itemData ->
                Column(
                    Modifier
                        .size(150.dp)
                        .padding(5.dp)
                ) {
                    CoilImage(
                        imgUrl = itemData.albumImgUrl,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentDescription = ""
                    )
                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), contentAlignment = Alignment.Center
                ) {
                    FilledTonalButton(onClick = { isSearchActive.value = true }) {
                        Text(
                            text = "Add Music to this list",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}