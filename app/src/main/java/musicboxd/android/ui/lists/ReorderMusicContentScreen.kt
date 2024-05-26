package musicboxd.android.ui.lists

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderMusicContentScreen(createANewListScreenViewModel: CreateANewListScreenViewModel) {
    val lazyGridState = rememberLazyGridState()
    val list = createANewListScreenViewModel.currentSelection
    val draggingIndex = remember { mutableIntStateOf(-1) }
    val targetIndex = remember { mutableIntStateOf(-1) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }, title = {
            Text(
                text = "Reorder the list",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge
            )
        })
    }, bottomBar = {
        BottomAppBar {

        }
        Surface(
            tonalElevation = BottomAppBarDefaults.ContainerElevation,
            modifier = Modifier
                .fillMaxWidth()
                .background(BottomAppBarDefaults.containerColor)
        ) {
            val tTile = try {
                list.value[targetIndex.intValue].albumTitle
            } catch (_: Exception) {
                "Start of the list"
            }
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    if (draggingIndex.intValue >= 0) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, top = 15.dp, end = 15.dp),
                                style = MaterialTheme.typography.titleSmall,
                                text = "Remove your finger for adding"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, top = 5.dp, end = 15.dp),
                                style = MaterialTheme.typography.titleLarge,
                                text = "${list.value[draggingIndex.intValue].albumTitle} in the position of $tTile"
                            )
                        }
                    }
                }
                FilledTonalButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text(
                        text = "Save this order",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            columns = GridCells.Fixed(4),
            state = lazyGridState
        ) {
            itemsIndexed(list.value) { index, itemData ->
                val currentItemOffSet = remember {
                    Pair(mutableFloatStateOf(0f), mutableFloatStateOf(0f))
                }
                Box(modifier = Modifier
                    .zIndex(if (draggingIndex.intValue == index) 1f else 0f)
                    .size(150.dp)
                    .padding(2.dp)
                    .offset(
                        x = currentItemOffSet.first.floatValue.dp,
                        y = currentItemOffSet.second.floatValue.dp
                    )
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(onDragStart = {
                            draggingIndex.intValue = index
                        }, onDrag = { change, dragAmount ->
                            change.consume()
                            if (draggingIndex.intValue == index) {
                                currentItemOffSet.second.floatValue += dragAmount.y / 2
                                currentItemOffSet.first.floatValue += dragAmount.x / 2
                            }
                            val offset = Offset(
                                lazyGridState.layoutInfo.visibleItemsInfo[index].offset.x + (currentItemOffSet.first.floatValue * 2),
                                lazyGridState.layoutInfo.visibleItemsInfo[index].offset.y + (currentItemOffSet.second.floatValue * 2)
                            )
                            val targetItemIndex =
                                lazyGridState.layoutInfo.visibleItemsInfo.firstOrNull {
                                    it.offset.toOffset() == findNearestOffset(
                                        offset,
                                        lazyGridState.layoutInfo.visibleItemsInfo.map { it.offset.toOffset() })
                                }
                            targetItemIndex?.let {
                                targetIndex.intValue = it.index
                            }
                            Log.d(
                                "10MinMail",
                                "${currentItemOffSet.first.floatValue * 2}, ${currentItemOffSet.second.floatValue * 2}\nNearest value: ${
                                    findNearestOffset(
                                        offset,
                                        lazyGridState.layoutInfo.visibleItemsInfo.map { it.offset.toOffset() })
                                }\nTarget Index = ${targetItemIndex?.index}"
                            )
                        }, onDragEnd = {
                            val targetItemIndex =
                                lazyGridState.layoutInfo.visibleItemsInfo.firstOrNull {
                                    it.offset.toOffset() == findNearestOffset(
                                        Offset(
                                            (currentItemOffSet.first.floatValue * (2 + index)),
                                            (currentItemOffSet.second.floatValue * (2 + index))
                                        ),
                                        lazyGridState.layoutInfo.visibleItemsInfo.map { it.offset.toOffset() })
                                }
                            currentItemOffSet.first.floatValue = 0f
                            currentItemOffSet.second.floatValue = 0f
                            targetItemIndex?.let {
                                targetIndex.intValue = it.index
                                val newList =
                                    createANewListScreenViewModel.currentSelection.value.toMutableList()
                                newList.add(
                                    targetItemIndex.index,
                                    createANewListScreenViewModel.currentSelection.value[draggingIndex.intValue]
                                )
                                createANewListScreenViewModel.currentSelection.value =
                                    newList.distinct()
                            }
                            Log.d("10MinMail",
                                lazyGridState.layoutInfo.visibleItemsInfo
                                    .map { it.offset }
                                    .toString()
                            )
                            draggingIndex.intValue = -1
                        })
                    }
                    .clip(RoundedCornerShape(5.dp))) {
                    CoilImage(
                        imgUrl = itemData.albumImgUrl,
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (draggingIndex.intValue != index) Modifier.fadedEdges(
                                    MaterialTheme.colorScheme
                                ) else Modifier
                            ),
                        contentDescription = ""
                    )
                    if (draggingIndex.intValue != index) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .size(24.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                .align(Alignment.BottomCenter),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun findNearestOffset(currentOffset: Offset, existingOffsets: List<Offset>): Offset {
    fun euclideanDistance(offset1: Offset, offset2: Offset): Double {
        val dx = (offset2.x - offset1.x).toDouble()
        val dy = (offset2.y - offset1.y).toDouble()
        return sqrt(dx * dx + dy * dy)
    }

    return existingOffsets.minBy { offset ->
        euclideanDistance(currentOffset, offset)
    }
}