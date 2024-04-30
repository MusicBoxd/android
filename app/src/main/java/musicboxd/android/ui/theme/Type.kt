package musicboxd.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import musicboxd.android.R

val fonts = FontFamily(
    Font(R.font.semibold, weight = FontWeight.SemiBold),
    Font(R.font.black, weight = FontWeight.Black),
    Font(R.font.bold, weight = FontWeight.Bold),
    Font(R.font.regular, weight = FontWeight.Normal),
)
val Typography = Typography(
    titleLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Normal),
)