package musicboxd.android.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationVM: NavigationVM = viewModel()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .background(
                    colorScheme.surfaceColorAtElevation(
                        NavigationBarDefaults.Elevation
                    )
                )
                .padding(start = 2.dp)
        )
        navigationVM.btmBarList.forEach {
            NavigationBarItem(
                alwaysShowLabel = false,
                selected = currentRoute == it.navigationRoute.name, onClick = {
                    if (currentRoute != it.navigationRoute.name) navController.navigate(it.navigationRoute.name)
                }, icon = {
                    Icon(
                        imageVector = if (currentRoute == it.navigationRoute.name) {
                            it.selectedIcon
                        } else {
                            it.nonSelectedIcon
                        }, contentDescription = null
                    )
                }, label = {
                    Text(
                        text = it.itemName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1
                    )
                })
        }
        Spacer(
            modifier = Modifier
                .background(
                    colorScheme.surfaceColorAtElevation(
                        NavigationBarDefaults.Elevation
                    )
                )
                .padding(start = 2.dp)
        )
    }
}