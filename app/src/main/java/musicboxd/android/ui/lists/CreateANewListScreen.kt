package musicboxd.android.ui.lists

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import musicboxd.android.ui.review.BooleanPreferenceGroup
import musicboxd.android.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateANewListScreen() {
    val newListTile = rememberSaveable {
        mutableStateOf("")
    }
    val newListTitleInteractionSource = remember {
        MutableInteractionSource()
    }
    val newListDescription = rememberSaveable {
        mutableStateOf("")
    }
    val newListDescriptionInteractionSource = remember {
        MutableInteractionSource()
    }
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(Modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = "Create a new List",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }, topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }, title = {
            Text(
                text = "Create a new List",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge
            )
        })
    }) {
        if (isSearchActive.value) {

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = "Name of the List",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )
            BasicTextField(
                interactionSource = newListTitleInteractionSource,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                value = newListTile.value,
                onValueChange = { newValue ->
                    newListTile.value = newValue
                },
                textStyle = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = LocalContentColor.current
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                color = if (newListTitleInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    0.65f
                )
            )
            Text(
                text = "Description of the List",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )
            BasicTextField(
                interactionSource = newListDescriptionInteractionSource,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 75.dp)
                    .padding(start = 15.dp, end = 15.dp),
                value = newListDescription.value,
                onValueChange = { newValue ->
                    newListDescription.value = newValue
                },
                textStyle = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = LocalContentColor.current
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                color = if (newListDescriptionInteractionSource.collectIsFocusedAsState().value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(
                    0.65f
                )
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Save as a Public List",
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(checked = true, onCheckedChange = {})
            }
            Text(
                text = "Is this a numbered List?",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )
            BooleanPreferenceGroup {

            }
            Text(
                text = "Music Content",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), contentAlignment = Alignment.Center
            ) {
                FilledTonalButton(onClick = { isSearchActive.value = true }) {
                    Text(
                        text = "Add Music to this list",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}