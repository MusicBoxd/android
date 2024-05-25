package musicboxd.android.ui.lists

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
fun ReorderMusicContentScreen() {
    val lazyGridState = rememberLazyGridState()
    val list = remember {
        mutableStateListOf(
            "https://media.gq-magazine.co.uk/photos/62bb1ff22176d0b26a09d6c7/master/w_1600,c_limit/MMLP1_alternate_cover.jpg",
            "https://media.gq-magazine.co.uk/photos/62bb1f2d916a16217c0cec49/master/w_1600,c_limit/c2dbe79ace8a4998c9955214bf6ee345.jpg",
            "https://media.gq-magazine.co.uk/photos/5eb94fa161ee223ac16caad2/master/w_1600,c_limit/20200511-album-14.jpg",
            "https://media.gq-magazine.co.uk/photos/5eb94fa31578fa0ec3478b81/master/w_1600,c_limit/20200511-album-04.jpg",
            "https://media.gq-magazine.co.uk/photos/61f007542bcbf0978c2b7b2d/master/w_1600,c_limit/250122_hiphop_04.jpg",
            "https://media.gq-magazine.co.uk/photos/61f007543d7efe70a7943d28/master/w_1600,c_limit/250122_hiphop_01.jpg"
        )
    }
    val offsets = remember {
        list.reversed().plus(list.shuffled()).plus(list).map {
            Pair(mutableFloatStateOf(0f), mutableFloatStateOf(0f))
        }
    }
    val draggingIndex = remember { mutableIntStateOf(-1) }
    val currentItemOffSetX = remember { mutableIntStateOf(0) }
    val selectedItemIndex = remember {
        mutableIntStateOf(0)
    }
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
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
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
    }) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            columns = GridCells.Fixed(4),
            state = lazyGridState
        ) {
            itemsIndexed(list + list.shuffled() + list.reversed()) { index, itemData ->
                val currentItemOffSet = offsets[index]
                Box(modifier = Modifier
                    .zIndex(if (selectedItemIndex.intValue == index) 1f else 0f)
                    .size(150.dp)
                    .padding(2.dp)
                    .offset(
                        x = currentItemOffSet.first.floatValue.dp,
                        y = currentItemOffSet.second.floatValue.dp
                    )
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(onDragStart = {
                            draggingIndex.intValue = index
                            selectedItemIndex.intValue =
                                lazyGridState.layoutInfo.visibleItemsInfo.first { it.index == index }.index
                        }, onDrag = { change, dragAmount ->
                            change.consume()
                            if (draggingIndex.intValue == index) {
                                currentItemOffSet.second.floatValue += dragAmount.y / 2
                                currentItemOffSet.first.floatValue += dragAmount.x / 2
                            }
                            currentItemOffSetX.intValue = try {
                                lazyGridState.layoutInfo.visibleItemsInfo.first { it.index == index }.offset.y
                            } catch (_: Exception) {
                                currentItemOffSetX.intValue
                            }
                            Log.d(
                                "10MinMail",
                                "${currentItemOffSet.first.floatValue * 2}, ${currentItemOffSet.second.floatValue * 2}\n Nearliest value: ${
                                    findNearestOffset(
                                        Offset(
                                            (currentItemOffSet.first.floatValue * 2),
                                            (currentItemOffSet.second.floatValue * 2)
                                        ),
                                        lazyGridState.layoutInfo.visibleItemsInfo.map { it.offset.toOffset() })
                                } "
                            )
                            Log.d("10MinMail",
                                lazyGridState.layoutInfo.visibleItemsInfo
                                    .map { it.offset }
                                    .toString()
                            )
                        }, onDragEnd = {
                            val targetItemIndex =
                                lazyGridState.layoutInfo.visibleItemsInfo.firstOrNull {
                                    it.offset.toOffset() == findNearestOffset(
                                        Offset(
                                            (currentItemOffSet.first.floatValue * 2),
                                            (currentItemOffSet.second.floatValue * 2)
                                        ),
                                        lazyGridState.layoutInfo.visibleItemsInfo.map { it.offset.toOffset() })
                                }
                            targetItemIndex?.let {
                                val tempValue = list[it.index]
                                list[it.index] = list[selectedItemIndex.intValue]
                                list[selectedItemIndex.intValue] = tempValue
                            }
                            currentItemOffSetX.intValue = 0
                            currentItemOffSet.second.floatValue = 0f
                            currentItemOffSet.first.floatValue = 0f
                            draggingIndex.intValue = -1
                        })
                    }
                    .clip(RoundedCornerShape(5.dp))) {
                    CoilImage(
                        imgUrl = itemData,
                        modifier = Modifier
                            .fillMaxSize()
                            .fadedEdges(MaterialTheme.colorScheme),
                        contentDescription = ""
                    )
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
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

private fun findNearestOffset(currentOffset: Offset, existingOffsets: List<Offset>): Offset {
    return existingOffsets.minByOrNull { offset ->
        val dx = (offset.x - currentOffset.x).toDouble()
        val dy = (offset.y - currentOffset.y).toDouble()
        sqrt(dx * dx + dy * dy) // euclideanDistance
    } ?: existingOffsets.first()
}