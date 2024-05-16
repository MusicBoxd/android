package musicboxd.android.ui.user.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import musicboxd.android.ui.theme.fonts
import musicboxd.android.utils.toBase64
import musicboxd.android.utils.toBitMap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditProfile(editProfileViewModel: EditProfileViewModel = viewModel()) {
    val profilePicUri = remember {
        mutableStateOf(Uri.EMPTY)
    }
    val headerPicUri = remember {
        mutableStateOf(Uri.EMPTY)
    }
    val pfpPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            profilePicUri.value = it
        }
    val headerPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            headerPicUri.value = it
        }
    val localContext = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = ""
                )
            }
        }, title = {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
            )
        }, actions = {
            FilledTonalButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(end = 10.dp)) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        })
    }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(165.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (headerPicUri.value.toString().isNotEmpty()) {
                        headerPicUri.value.toBase64(localContext).toBitMap()?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentDescription = "",
                                alignment = Alignment.Center
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(0.5f))
                    )
                    IconButton(
                        onClick = { headerPickerLauncher.launch("image/*") }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = ""
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(85.dp)
                        .align(Alignment.BottomStart), contentAlignment = Alignment.Center
                ) {
                    if (profilePicUri.value.toString().isNotEmpty()) {
                        profilePicUri.value.toBase64(localContext).toBitMap()?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(85.dp)
                                    .clip(CircleShape)
                                    .border(
                                        BorderStroke(2.5.dp, LocalContentColor.current), CircleShape
                                    )
                                    .align(Alignment.BottomStart),
                                alignment = Alignment.TopCenter
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .height(85.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(0.5f))
                    )
                    IconButton(
                        onClick = { pfpPickerLauncher.launch("image/*") }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = ""
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            editProfileViewModel.editProfileState.forEach {
                Text(
                    text = it.editItemName,
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    value = it.editItemValue.value,
                    onValueChange = { newValue ->
                        it.editItemValue.value = newValue
                    },
                    textStyle = TextStyle(fontFamily = fonts)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}