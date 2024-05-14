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
import musicboxd.android.ui.search.SearchScreen
import musicboxd.android.ui.search.charts.ChartsScreen
import musicboxd.android.ui.search.charts.ChartsScreenViewModel

@Composable
fun MainNavigation(
    navController: NavHostController,
    detailsViewModel: DetailsViewModel,
    chartsScreenViewModel: ChartsScreenViewModel
) {
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
            SearchScreen(
                navController = navController,
                detailsViewModel = detailsViewModel,
                searchScreenViewModel = detailsViewModel,
                chartsScreenViewModel = chartsScreenViewModel
            )
        }
        composable(route = NavigationRoutes.ALBUM_DETAILS.name) {
            AlbumDetailScreen(detailsViewModel.albumScreenState, detailsViewModel, navController)
        }
        composable(route = NavigationRoutes.ARTIST_DETAILS.name) {
            ArtistDetailScreen(detailsViewModel, navController)
        }
        composable(route = NavigationRoutes.VIDEO_CANVAS.name) {
            VideoCanvas(detailsViewModel, navController)
        }
        composable(route = NavigationRoutes.CHARTS_SCREEN.name) {
            ChartsScreen(chartsScreenViewModel, detailsViewModel, navController)
        }
    }
}