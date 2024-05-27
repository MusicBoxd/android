package musicboxd.android.ui.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import musicboxd.android.ui.notifications.common.FromArtistComposable
import musicboxd.android.ui.notifications.common.NewReleaseComposable
import musicboxd.android.ui.notifications.common.OnTourInfoComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notificationsScreenViewModel: NotificationsScreenViewModel = hiltViewModel()
) {
    val notificationList = notificationsScreenViewModel.sampleList.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                ) {
                    Text(
                        text = "What's New",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Latest information related to the artists you subscribed to, you may have missed or we may have missed sending any notification, here its covered up.",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    text = "Today", style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                FromArtistComposable()
            }
            item {
                Text(
                    text = "Albums", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 45.dp, top = 15.dp),
                    fontSize = 18.sp
                )
            }
            item {
                NewReleaseComposable()
            }

            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 65.dp, end = 65.dp, top = 10.dp)
                )
            }

            item {
                Text(
                    text = "Concerts", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 45.dp, top = 10.dp),
                    fontSize = 18.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
            }

            items(notificationList.value) {
                OnTourInfoComposable(it)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}