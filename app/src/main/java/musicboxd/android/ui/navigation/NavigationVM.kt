package musicboxd.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
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
            itemName = "Home",
            selectedIcon = Icons.Filled.Home,
            nonSelectedIcon = Icons.Outlined.Home,
            navigationRoute = NavigationRoutes.HOME
        ),
        BtmNavigationItem(
            itemName = "Search",
            selectedIcon = Icons.Filled.Search,
            nonSelectedIcon = Icons.Outlined.Search,
            navigationRoute = NavigationRoutes.SEARCH
        ),
        BtmNavigationItem(
            itemName = "Add",
            selectedIcon = Icons.Filled.Add,
            nonSelectedIcon = Icons.Outlined.Add,
            navigationRoute = NavigationRoutes.ADD
        ),
        BtmNavigationItem(
            itemName = "Cues",
            selectedIcon = Icons.Filled.Notifications,
            nonSelectedIcon = Icons.Outlined.Notifications,
            navigationRoute = NavigationRoutes.CUES
        ),
    )

}