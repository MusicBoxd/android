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
import musicboxd.android.ui.home.HomeScreen
import musicboxd.android.ui.lists.CreateANewListScreen
import musicboxd.android.ui.lists.CreateANewListScreenViewModel
import musicboxd.android.ui.lists.ReorderMusicContentScreen
import musicboxd.android.ui.notifications.NotificationsScreen
import musicboxd.android.ui.review.AddANewReviewScreen
import musicboxd.android.ui.review.AddScreen
import musicboxd.android.ui.search.SearchScreen
import musicboxd.android.ui.search.charts.ChartsScreen
import musicboxd.android.ui.startup.StartUpScreen
import musicboxd.android.ui.startup.login.SignInScreen
import musicboxd.android.ui.user.profile.UserProfile
import musicboxd.android.ui.user.profile.editProfile.EditProfile
import musicboxd.android.ui.user.profile.editProfile.EditProfileViewModel

@Composable
fun MainNavigation(
    navController: NavHostController,
    detailsViewModel: DetailsViewModel,
    editProfileViewModel: EditProfileViewModel,
    createANewListScreenViewModel: CreateANewListScreenViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.START_UP.name
    ) {
        composable(route = NavigationRoutes.HOME.name) {
            HomeScreen()
        }
        composable(route = NavigationRoutes.SETTINGS.name) {
            Column {

            }
        }
        composable(route = NavigationRoutes.ADD.name) {
            AddScreen(
                navController = navController,
                detailsViewModel = detailsViewModel,
                searchScreenViewModel = detailsViewModel,
                createANewListScreenViewModel
            )
        }
        composable(route = NavigationRoutes.CUES.name) {
            NotificationsScreen()
        }
        composable(route = NavigationRoutes.PROFILE.name) {
            UserProfile(navController)
        }
        composable(route = NavigationRoutes.SEARCH.name) {
            SearchScreen(
                navController = navController,
                detailsViewModel = detailsViewModel,
                searchScreenViewModel = detailsViewModel,
                chartsScreenViewModel = detailsViewModel
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
            ChartsScreen(detailsViewModel, navController)
        }
        composable(route = NavigationRoutes.EDIT_PROFILE.name) {
            EditProfile(editProfileViewModel, navController)
        }
        composable(route = NavigationRoutes.CREATE_A_NEW_LIST.name) {
            CreateANewListScreen(detailsViewModel, navController, createANewListScreenViewModel)
        }
        composable(route = NavigationRoutes.CREATE_A_NEW_REVIEW.name) {
            AddANewReviewScreen(navController, detailsViewModel)
        }
        composable(route = NavigationRoutes.REORDER_MUSIC_CONTENT_SCREEN.name) {
            ReorderMusicContentScreen(createANewListScreenViewModel)
        }
        composable(route = NavigationRoutes.START_UP.name) {
            StartUpScreen(navController = navController)
        }
        composable(route = NavigationRoutes.SIGN_IN.name) {
            SignInScreen(navController = navController)
        }
    }
}