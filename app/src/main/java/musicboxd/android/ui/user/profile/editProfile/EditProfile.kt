package musicboxd.android.ui.user.profile.editProfile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import musicboxd.android.ui.theme.fonts
import musicboxd.android.utils.toBase64
import musicboxd.android.utils.toBitMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(editProfileViewModel: EditProfileViewModel, navController: NavController) {

    val pfpImageCropperLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) {
            if (it.isSuccessful) {
                editProfileViewModel.profilePicUri.value = it.uriContent
            }
        }
    val headerImageCropperLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) {
            if (it.isSuccessful) {
                editProfileViewModel.headerPicUri.value = it.uriContent
            }
        }
    val colorScheme = MaterialTheme.colorScheme
    val topAppBarColors = TopAppBarDefaults.topAppBarColors()
    val pfpPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            if (it != null) {
                pfpImageCropperLauncher.launch(
                    CropImageContractOptions(
                        it,
                        CropImageOptions(
                            cropperLabelTextColor = topAppBarColors.titleContentColor.toArgb(),
                            progressBarColor = colorScheme.primary.toArgb(),
                            activityBackgroundColor = colorScheme.surface.toArgb(),
                            fixAspectRatio = true,
                            cropShape = CropImageView.CropShape.OVAL
                        )
                    )
                )
            }
        }
    val headerPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            if (it != null) {
                headerImageCropperLauncher.launch(
                    CropImageContractOptions(
                        it,
                        CropImageOptions(
                            progressBarColor = colorScheme.primary.toArgb(),
                            activityBackgroundColor = colorScheme.surface.toArgb(),
                            fixAspectRatio = true,
                            aspectRatioX = 3,
                            aspectRatioY = 1,
                        )
                    )
                )
            }
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
                    if (editProfileViewModel.headerPicUri.value != null && editProfileViewModel.headerPicUri.value.toString()
                            .isNotEmpty()
                    ) {
                        editProfileViewModel.headerPicUri.value.toBase64(localContext).toBitMap()
                            ?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clickable {
                                            headerPickerLauncher.launch("image/*")
                                        },
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
                    if (editProfileViewModel.profilePicUri.value != null && editProfileViewModel.profilePicUri.value.toString()
                            .isNotEmpty()
                    ) {
                        editProfileViewModel.profilePicUri.value.toBase64(localContext).toBitMap()
                            ?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(85.dp)
                                        .clip(CircleShape)
                                        .border(
                                            BorderStroke(2.5.dp, LocalContentColor.current),
                                            CircleShape
                                        )
                                        .align(Alignment.BottomStart)
                                        .clickable {
                                            pfpPickerLauncher.launch("image/*")
                                        },
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