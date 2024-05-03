package musicboxd.android.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.details.album.AlbumDetailScreen
import musicboxd.android.ui.details.artist.ArtistDetailScreen
import musicboxd.android.ui.details.canvas.VideoCanvas
import musicboxd.android.ui.details.track.TrackDetailScreen
import musicboxd.android.ui.search.SearchScreen

@Composable
fun MainNavigation(navController: NavHostController, detailsViewModel: DetailsViewModel) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.SEARCH.name
    ) {
        composable(route = NavigationRoutes.HOME.name) {
            Column {

            }
        }
        composable(route = NavigationRoutes.SETTINGS.name) {
            Column {

            }
        }
        composable(route = NavigationRoutes.SEARCH.name) {
            SearchScreen(navController = navController, detailsViewModel = detailsViewModel)
        }
        composable(route = NavigationRoutes.ALBUM_DETAILS.name) {
            AlbumDetailScreen(detailsViewModel.albumScreenState, detailsViewModel, navController)
        }
        composable(route = NavigationRoutes.TRACK_DETAILS.name) {
            TrackDetailScreen()
        }
        composable(route = NavigationRoutes.ARTIST_DETAILS.name) {
            ArtistDetailScreen()
        }
        composable(route = NavigationRoutes.VIDEO_CANVAS.name) {
            VideoCanvas(detailsViewModel)
        }
    }
}