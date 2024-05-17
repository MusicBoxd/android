package musicboxd.android.ui.review

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.search.SearchContent
import musicboxd.android.ui.search.SearchScreenUiEvent
import musicboxd.android.ui.search.SearchScreenViewModel
import musicboxd.android.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavController,
    detailsViewModel: DetailsViewModel,
    searchScreenViewModel: SearchScreenViewModel
) {
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    val searchQuery = searchScreenViewModel.searchQuery.collectAsState()
    val currentlySelectedItem = rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        if (currentlySelectedItem.value.isEmpty()) {
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
                                currentlySelectedItem.value = it
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
                    text = "Write a new Review",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge

                )
            })
        }
    }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (currentlySelectedItem.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column {
                        Text(
                            text = "Search for albums, tracks, or artists and share your reviews!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Or...",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleMedium
                        )
                        FilledTonalButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp)
                        ) {
                            Text(
                                text = "Create a New List",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        FilledTonalButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp)
                        ) {
                            Text(text = "See Drafts", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoilImage(
                    imgUrl = "https://i.scdn.co/image/ab67616d0000b2735ad8ab24fd5d0988af1f6fb7",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentDescription = ""
                )
                Column {
                    Text(
                        text = "Beloved! Paradise! Jazz!?",
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
                        text = "McKinley Dixon",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        color = LocalContentColor.current.copy(0.8f)
                    )
                }
            }
            val reviewTitle = rememberSaveable {
                mutableStateOf("")
            }
            val reviewTitleInteractionSource = remember {
                MutableInteractionSource()
            }
            val reviewContent = rememberSaveable {
                mutableStateOf("")
            }
            val reviewContentInteractionSource = remember {
                MutableInteractionSource()
            }
            Text(
                text = "Review Title",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )

            BasicTextField(
                interactionSource = reviewTitleInteractionSource,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                value = reviewTitle.value,
                onValueChange = { newValue ->
                    reviewTitle.value = newValue
                },
                textStyle = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = LocalContentColor.current
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                color = if (reviewTitleInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    0.65f
                )
            )

            Text(
                text = "Review",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )

            BasicTextField(
                interactionSource = reviewContentInteractionSource,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .padding(start = 15.dp, end = 15.dp),
                value = reviewContent.value,
                onValueChange = { newValue ->
                    reviewContent.value = newValue
                },
                textStyle = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = LocalContentColor.current
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                color = if (reviewContentInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    0.65f
                )
            )
            val albumLikeStatus: MutableState<Boolean?> = rememberSaveable {
                mutableStateOf(null)
            }
            val albumRecommendationStatus: MutableState<Boolean?> = rememberSaveable {
                mutableStateOf(null)
            }
            Text(
                text = "Did you like this album? ${albumLikeStatus.value}",
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 10.dp,
                    end = 15.dp
                ),
                style = MaterialTheme.typography.titleMedium
            )
            BooleanPreferenceGroup(preference = {
                albumLikeStatus.value = it
            })
            Text(
                text = "Would you recommend this to other people? ${albumRecommendationStatus.value}",
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 10.dp,
                    end = 15.dp
                ),
                style = MaterialTheme.typography.titleMedium
            )
            BooleanPreferenceGroup(preference = {
                albumRecommendationStatus.value = it
            })
            val ratingValue = rememberSaveable {
                mutableFloatStateOf(0f)
            }
            Text(
                text = "How much would you rate this record?",
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 10.dp,
                    end = 15.dp
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Row(verticalAlignment = Alignment.Bottom) {
                RatingBar(
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp),
                    config = RatingBarConfig().inactiveColor(MaterialTheme.colorScheme.outline)
                        .activeColor(MaterialTheme.colorScheme.primary).padding(5.dp)
                        .stepSize(StepSize.HALF).style(RatingBarStyle.HighLighted),
                    value = ratingValue.floatValue,
                    onValueChange = {
                        ratingValue.floatValue = it
                    },
                    onRatingChanged = {

                    })
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${ratingValue.floatValue}/5.0",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            val reviewTags = rememberSaveable {
                mutableStateOf("")
            }
            val reviewTagsInteractionSource = remember {
                MutableInteractionSource()
            }
            Text(
                text = "Tags",
                modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 5.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                text = buildAnnotatedString {
                    append("Separate multiple tags by adding a comma")
                    append(" (")
                    withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                        append(" ,")
                    }
                    append(" )")
                },
                style = MaterialTheme.typography.titleSmall
            )

            BasicTextField(
                interactionSource = reviewTagsInteractionSource,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                value = reviewTags.value,
                onValueChange = { newValue ->
                    reviewTags.value = newValue
                },
                textStyle = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = LocalContentColor.current
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                color = if (reviewTagsInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    0.65f
                )
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "")
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Your review has already been auto-saved locally on your device. If you want to make improvements, always go back to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Drafts")
                        }
                        append(".\n\nOnce you post your review, it cannot be changed.")
                    },
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Button(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            ) {
                Text(text = "Post Review", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun BooleanPreferenceGroup(preference: (Boolean?) -> Unit) {
    val firstBooleanPref = rememberSaveable {
        mutableStateOf(false)
    }
    val secondBooleanPref = rememberSaveable {
        mutableStateOf(false)
    }
    preference(if (!firstBooleanPref.value && !secondBooleanPref.value) null else firstBooleanPref.value)
    val firstPrefColor =
        if (firstBooleanPref.value) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val secondPrefColor =
        if (secondBooleanPref.value) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(15.dp))
        OutlinedButton(
            shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp),
            onClick = {
                firstBooleanPref.value = true
                secondBooleanPref.value = false
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = firstPrefColor)
        ) {
            Text(
                text = "Yes",
                style = MaterialTheme.typography.titleLarge,
                color = if (firstBooleanPref.value) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        OutlinedButton(
            shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp),
            onClick = {
                firstBooleanPref.value = false
                secondBooleanPref.value = true
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = secondPrefColor)
        ) {
            Text(
                text = "No",
                style = MaterialTheme.typography.titleLarge,
                color = if (secondBooleanPref.value) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}