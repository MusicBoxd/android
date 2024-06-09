package musicboxd.android.ui.startup

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.channels.consumeEach
import musicboxd.android.data.remote.api.musicboxd.model.user.SignUpDTO
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.theme.fonts

@Composable
fun StartUpScreen(
    navController: NavController,
    startUpVM: StartUpVM = hiltViewModel()
) {
    val checkingForActiveSession = rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = true) {
        startUpVM.uiEvent.consumeEach {
            when (it) {
                is StartUpScreenEvent.CheckingIfAnySessionAlreadyExists -> checkingForActiveSession.value =
                    true

                is StartUpScreenEvent.Navigate -> navController.navigate(it.navigationRoute) {
                    popUpTo(0)
                }

                is StartUpScreenEvent.None -> checkingForActiveSession.value = false
                else -> Unit
            }
        }
    }
    val uiEvent = startUpVM.uiEventAsFlow.collectAsState(initial = StartUpScreenEvent.None)
    val profileName = rememberSaveable {
        mutableStateOf("")
    }
    val username = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val showProgressBarStatus = startUpVM.showProgressBarInBottomBar.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (!checkingForActiveSession.value) {
            BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                if (showProgressBarStatus.value) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                } else {
                    androidx.compose.material3.Button(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        onClick = {
                            startUpVM.fromUIEvent(
                                StartUpScreenEvent.SignUp(
                                    SignUpDTO(
                                        userProfileName = profileName.value,
                                        username = username.value,
                                        password = password.value
                                    )
                                )
                            )
                        }) {
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = if (!checkingForActiveSession.value) Alignment.Center else Alignment.BottomCenter
        ) {
            if (!checkingForActiveSession.value) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(start = 20.dp, end = 20.dp)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                            ) {
                                append("Welcome to\n")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 26.sp
                                )
                            ) {
                                append("MusicBoxd")
                            }
                        }, style = MaterialTheme.typography.titleMedium, fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier)
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp
                    )
                    ProvideTextStyle(value = TextStyle(fontFamily = fonts)) {

                        OutlinedTextField(value = profileName.value, onValueChange = {
                            profileName.value = it
                        }, modifier = Modifier.fillMaxWidth(), label = {
                            Text(
                                text = "Your name",
                                style = MaterialTheme.typography.titleSmall
                            )
                        })

                        OutlinedTextField(value = username.value, onValueChange = {
                            username.value = it
                        }, modifier = Modifier.fillMaxWidth(), label = {
                            Text(
                                text = "Username",
                                style = MaterialTheme.typography.titleSmall
                            )
                        })

                        OutlinedTextField(value = password.value, onValueChange = {
                            password.value = it
                        }, modifier = Modifier.fillMaxWidth(), label = {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.titleSmall
                            )
                        })
                    }

                    Spacer(modifier = Modifier)

                    TextButton(onClick = {
                        navController.navigate(NavigationRoutes.SIGN_IN.name)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Sign in",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "MusicBoxd",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 26.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Checking for an existing local active session",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
        val context = LocalContext.current
        BackHandler {
            navController.backQueue.removeIf {
                true
            }
            val activity = context as Activity
            activity.moveTaskToBack(false)
        }
    }
}