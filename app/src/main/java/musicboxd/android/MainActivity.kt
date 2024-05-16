package musicboxd.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import musicboxd.android.data.RefreshReleasesWorker
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.BottomNavigationBar
import musicboxd.android.ui.navigation.MainNavigation
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.theme.MusicBoxdTheme
import musicboxd.android.ui.user.profile.editProfile.EditProfileViewModel
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val scaffoldState = rememberBottomSheetScaffoldState()
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            val currentNavRoute = rememberSaveable(
                inputs = arrayOf(currentBackStackEntry.value?.destination?.route)
            ) {
                currentBackStackEntry.value?.destination?.route.toString()
            }
            val mainNavRoutes = rememberSaveable {
                listOf(
                    NavigationRoutes.HOME.name,
                    NavigationRoutes.SEARCH.name,
                    NavigationRoutes.ADD.name,
                    NavigationRoutes.CUES.name,
                    NavigationRoutes.PROFILE.name
                )
            }
            LaunchedEffect(key1 = currentNavRoute) {
                if (mainNavRoutes.contains(currentNavRoute)) {
                    scaffoldState.bottomSheetState.expand()
                } else {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            val editProfileViewModel: EditProfileViewModel = viewModel()
            MusicBoxdTheme {
                Scaffold(Modifier.fillMaxSize()) {
                    androidx.compose.material.BottomSheetScaffold(sheetPeekHeight = 0.dp,
                        scaffoldState = scaffoldState,
                        sheetContent = {
                            BottomNavigationBar(navController = navController)
                        }) {
                        Scaffold {
                            MainNavigation(
                                navController = navController,
                                detailsViewModel,
                                editProfileViewModel
                            )
                        }
                    }
                }
            }
        }
        val latestReleasesNotificationWorker =
            PeriodicWorkRequest.Builder(RefreshReleasesWorker::class.java, 15, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "releaseChecker", ExistingPeriodicWorkPolicy.REPLACE, latestReleasesNotificationWorker
        )
    }
}