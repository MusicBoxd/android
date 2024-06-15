package musicboxd.android.ui.navigation.drawer

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerModel(
    val name: String,
    val imgVector: ImageVector,
    val onClick: () -> Unit
)
