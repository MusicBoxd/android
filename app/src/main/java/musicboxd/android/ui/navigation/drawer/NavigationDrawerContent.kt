package musicboxd.android.ui.navigation.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.theme.MusicBoxdTheme

@Composable
fun NavigationDrawerContent(navController: NavController, navigationDrawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    val navigationDrawerContents = listOf(
        NavigationDrawerModel(
            name = "Profile",
            imgVector = Icons.Default.AccountCircle,
            onClick = {
                navController.navigate(NavigationRoutes.PROFILE.name)
            }),
        NavigationDrawerModel(
            name = "Saved Events",
            imgVector = Icons.Default.Event,
            onClick = {
                navController.navigate(NavigationRoutes.SAVED_EVENTS.name)

            }),
        NavigationDrawerModel(
            name = "Drafts",
            imgVector = Icons.Default.Drafts,
            onClick = {
                navController.navigate(NavigationRoutes.DRAFTS.name)

            }),
        NavigationDrawerModel(
            name = "Subscriptions",
            imgVector = Icons.Default.PersonAdd,
            onClick = {

            }),
    )
    MusicBoxdTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.85f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            CoilImage(
                imgUrl = "https://pbs.twimg.com/profile_images/1801650747291090944/F9sc3CDc_400x400.jpg",
                modifier = Modifier
                    .padding(start = 25.dp)
                    .size(50.dp)
                    .clip(CircleShape),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Nasir Jones",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 25.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "@nas",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(start = 25.dp),
                color = LocalContentColor.current.copy(0.65f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row {
                Text(
                    text = buildAnnotatedString {
                        append("217 ")
                        withStyle(SpanStyle(color = LocalContentColor.current.copy(0.65f))) {
                            append("Following")
                        }
                        append("\t\t")
                        append("101 ")
                        withStyle(SpanStyle(color = LocalContentColor.current.copy(0.65f))) {
                            append("Followers")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(start = 25.dp)
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            )
            navigationDrawerContents.forEach {
                NavigationDrawerItem(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 25.dp,
                        bottomEnd = 25.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.85f), label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(25.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = it.imgVector,
                                contentDescription = it.name,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }, selected = false, onClick = {
                        it.onClick()
                        coroutineScope.launch {
                            navigationDrawerState.close()
                        }
                    })
                Spacer(modifier = Modifier.height(10.dp))
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 15.dp), contentAlignment = Alignment.BottomCenter
            ) {
                NavigationDrawerItem(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 25.dp,
                        bottomEnd = 25.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.85f), label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(25.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "Settings",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }, selected = false, onClick = { })
            }
        }
    }
}