package musicboxd.android.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen() {
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
    val publicReviews = homeScreenViewModel.publicReviews.collectAsStateWithLifecycle()
    LazyColumn(Modifier.fillMaxSize()) {
        items(publicReviews.value) {
            Text(
                text = "${it.albumId} - ${it.albumRating} - ${it.albumReview}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(15.dp)
            )
            Divider(modifier = Modifier.fillMaxSize())
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}