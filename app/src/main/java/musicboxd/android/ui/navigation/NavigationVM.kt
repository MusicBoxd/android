package musicboxd.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel


data class BtmNavigationItem(
    val selectedIcon: ImageVector,
    val nonSelectedIcon: ImageVector,
    val navigationRoute: NavigationRoutes,
    val itemName: String,
)

class NavigationVM : ViewModel() {

    val btmBarList = listOf(
        BtmNavigationItem(
            itemName = "Search",
            selectedIcon = Icons.Filled.Search,
            nonSelectedIcon = Icons.Outlined.Search,
            navigationRoute = NavigationRoutes.SEARCH
        ),
        BtmNavigationItem(
            itemName = "Home",
            selectedIcon = Icons.Filled.Home,
            nonSelectedIcon = Icons.Outlined.Home,
            navigationRoute = NavigationRoutes.HOME
        ),
        BtmNavigationItem(
            itemName = "Settings",
            selectedIcon = Icons.Filled.Settings,
            nonSelectedIcon = Icons.Outlined.Settings,
            navigationRoute = NavigationRoutes.SETTINGS
        ),
    )

}