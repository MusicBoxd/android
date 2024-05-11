package musicboxd.android.ui.search.charts

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.search.charts.billboard.model.Data

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(chartsScreenViewModel: ChartsScreenViewModel) {
    val billBoardChartsData = chartsScreenViewModel.billBoardData.collectAsStateWithLifecycle()
    val topAppBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(scrollBehavior = topAppBarScrollBehaviour, title = {
                Text(
                    text = chartsScreenViewModel.chartTitle.value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
            })
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = billBoardChartsData.value.date,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            itemsIndexed(billBoardChartsData.value.data) { index, data ->
                ChartItem(
                    data = data,
                    index = index,
                    isArtist = chartsScreenViewModel.chartTitle.value.contains("Artist")
                )
            }
        }
    }
}

@Composable
private fun ChartItem(data: Data, index: Int, isArtist: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Text(
            text = (index + 1).toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CoilImage(
                    imgUrl = data.itemImgURL,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(if (isArtist) CircleShape else RoundedCornerShape(5.dp)),
                    contentDescription = ""
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ChartPositionItem(title = "W.O.K", value = data.itemWeeksOnChart)
                    ChartPositionItem(title = "P.P", value = data.itemPeakPosition)
                    ChartPositionItem(title = "L.W", value = data.itemLastWeek)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = data.itemTitle,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (!isArtist) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = data.itemArtists,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        color = LocalContentColor.current.copy(0.25f)
    )
}

@Composable
private fun ChartPositionItem(title: String, value: String) {
    Spacer(modifier = Modifier.width(5.dp))
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = LocalContentColor.current.copy(0.8f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
    }
}