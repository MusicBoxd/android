package musicboxd.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import musicboxd.android.ui.navigation.BottomNavigationBar
import musicboxd.android.ui.navigation.MainNavigation
import musicboxd.android.ui.theme.MusicBoxdTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MusicBoxdTheme {
                Scaffold(bottomBar = {
                    BottomNavigationBar(navController = navController)
                }) {
                    MainNavigation(navController = navController)
                }
            }
        }
    }
}