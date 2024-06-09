package musicboxd.android.ui.startup.login

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import musicboxd.android.ui.startup.StartUpScreenEvent
import musicboxd.android.ui.theme.fonts

@Composable
fun SignInScreen(signInVM: SignInVM = hiltViewModel(), navController: NavController) {
    LaunchedEffect(key1 = true) {
        signInVM.uiEvent.receiveAsFlow().collectLatest {
            when (it) {
                is StartUpScreenEvent.Navigate -> navController.navigate(it.navigationRoute)
                else -> {}
            }
        }
    }
    val username = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val showProgressBarStatus = signInVM.showProgressBarInBottomBar.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            if (showProgressBarStatus.value) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            } else {
                Button(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    onClick = {
                        signInVM.signIn(username.value, password.value)
                    }) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.titleSmall
                    )
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
            contentAlignment = Alignment.Center
        ) {
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
                            append("Welcome back to\n")
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
                    text = "Sign in",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp
                )
                ProvideTextStyle(value = TextStyle(fontFamily = fonts)) {

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
            }
        }
    }
}