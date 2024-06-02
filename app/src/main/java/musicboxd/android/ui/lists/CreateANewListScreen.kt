package musicboxd.android.ui.lists

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.NavigationRoutes
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

    val newListTitleInteractionSource = remember {
        MutableInteractionSource()
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
    val uiChannel = createANewListScreenViewModel.uiEvent
    val localContext = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        uiChannel.collectLatest {
            when (it) {
                is CreateANewListScreenUIEvent.ShowToast -> {
                    Toast.makeText(localContext, it.msg, Toast.LENGTH_SHORT).show()
                }

                is CreateANewListScreenUIEvent.Nothing -> TODO()
            }
        }
    }
    val isAPublicList = createANewListScreenViewModel.isAPublicList.collectAsStateWithLifecycle()
    val isANumberedList =
        createANewListScreenViewModel.isANumberedList.collectAsStateWithLifecycle()
    Scaffold(Modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(
                onClick = {
                    createANewListScreenViewModel.publishANewList(
                        listName = createANewListScreenViewModel.newListTitle.value,
                        listDescription = createANewListScreenViewModel.newListDescription.value,
                        isListPublic = createANewListScreenViewModel.isAPublicList.value
                    )
                },
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
                                if (!createANewListScreenViewModel.currentMusicContentSelection.map { it.itemUri }
                                        .contains(detailsViewModel.albumScreenState.itemUri)
                                ) {
                                    createANewListScreenViewModel.currentMusicContentSelection += detailsViewModel.albumScreenState
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
    }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        Column(horizontalAlignment = Alignment.End) {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "Reorder",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                icon = {
                    Icon(imageVector = Icons.Default.Reorder, contentDescription = "")
                },
                onClick = { navController.navigate(NavigationRoutes.REORDER_MUSIC_CONTENT_SCREEN.name) })

            Spacer(modifier = Modifier.height(5.dp))
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "Add Music to this list",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "") },
                onClick = { isSearchActive.value = true })
        }
    }) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = 15.dp, end = 15.dp),
            columns = GridCells.Fixed(3),
            state = lazyGridState
        ) {
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = createANewListScreenViewModel.newListTitle.collectAsStateWithLifecycle().value,
                    onValueChange = { newValue ->
                        createANewListScreenViewModel.newListTitle.value = newValue
                    },
                    textStyle = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = LocalContentColor.current
                    ),
                    label = {
                        Text(
                            text = "Name of the List", style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Spacer(modifier = Modifier.height(15.dp))
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 75.dp),
                    value = createANewListScreenViewModel.newListDescription.collectAsStateWithLifecycle().value,
                    onValueChange = { newValue ->
                        createANewListScreenViewModel.newListDescription.value = newValue
                    },
                    textStyle = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = LocalContentColor.current
                    ),
                    label = {
                        Text(
                            text = "Description of the List",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                androidx.compose.material3.Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp)
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
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledTonalIconToggleButton(checked = isAPublicList.value,
                            onCheckedChange = {
                                createANewListScreenViewModel.isAPublicList.value = it
                            }) {
                            Icon(
                                imageVector = if (isAPublicList.value) Icons.Default.Public else Icons.Outlined.Lock,
                                ""
                            )
                        }
                        Text(
                            text = if (isAPublicList.value) "Public" else "Private",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(1.5.dp)
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.onSurface.copy(0.65f))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledTonalIconToggleButton(checked = isANumberedList.value,
                            onCheckedChange = {
                                createANewListScreenViewModel.isANumberedList.value = it
                            }) {
                            Icon(
                                imageVector = if (isANumberedList.value) Icons.Filled.Numbers else Icons.Outlined.Numbers,
                                ""
                            )
                        }
                        Text(
                            text = if (isANumberedList.value) "Numbered List" else "Non-numbered list",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                androidx.compose.material3.Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp)
                )
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                    Text(
                        text = "Music",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp
                    )
                Spacer(modifier = Modifier.height(28.dp))
            }
            itemsIndexed(
                createANewListScreenViewModel.currentMusicContentSelection,
                key = { index, it -> it.itemUri }) { index, itemData ->
                Column(Modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier =
                        Modifier
                            .size(100.dp)
                            .padding(top = 5.dp)
                    ) {
                        CoilImage(
                            imgUrl = itemData.albumImgUrl,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .then(
                                    if (isANumberedList.value) Modifier.fadedEdges(
                                        MaterialTheme.colorScheme
                                    ) else Modifier
                                ),
                            contentDescription = ""
                        )
                        if (isANumberedList.value) {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 5.dp)
                                    .size(24.dp)
                                    .background(
                                        ButtonDefaults.filledTonalButtonColors().containerColor,
                                        CircleShape
                                    )
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    FilledTonalIconButton(onClick = {
                            createANewListScreenViewModel.currentMusicContentSelection.removeAt(
                                index
                            )
                        }) {
                        Icon(
                            imageVector = Icons.Default.Cancel, contentDescription = ""
                        )
                    }
                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}